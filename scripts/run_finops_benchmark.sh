#!/usr/bin/env bash
set -euo pipefail

BASE_URL="${BASE_URL:-http://localhost:8080}"
VUS="${VUS:-20}"
DURATION="${DURATION:-30s}"
LAST_NAME="${LAST_NAME:-}"
OUT_DIR="${OUT_DIR:-fase_5/benchmark_results}"

K6_SCRIPT="scripts/benchmark/owners-search.js"
BASELINE_FILE="${OUT_DIR}/baseline.json"
OPTIMIZED_FILE="${OUT_DIR}/optimized.json"

if ! command -v k6 >/dev/null 2>&1; then
	echo "k6 is required. Install it first: https://grafana.com/docs/k6/latest/set-up/install-k6/"
	exit 1
fi

mkdir -p "${OUT_DIR}"

run_case() {
	local label="$1"
	local endpoint="$2"
	local output_file="$3"

	echo ""
	echo "Running ${label} benchmark against ${endpoint}"
	BASE_URL="${BASE_URL}" \
	ENDPOINT="${endpoint}" \
	VUS="${VUS}" \
	DURATION="${DURATION}" \
	LAST_NAME="${LAST_NAME}" \
	k6 run "${K6_SCRIPT}" --summary-export "${output_file}"
}

run_case "baseline" "/owners" "${BASELINE_FILE}"
run_case "optimized" "/only/owners" "${OPTIMIZED_FILE}"

echo ""
echo "Benchmark artifacts generated:"
echo "  - ${BASELINE_FILE}"
echo "  - ${OPTIMIZED_FILE}"

if command -v jq >/dev/null 2>&1; then
	base_avg="$(jq -r '.metrics.http_req_duration.values.avg' "${BASELINE_FILE}")"
	opt_avg="$(jq -r '.metrics.http_req_duration.values.avg' "${OPTIMIZED_FILE}")"
	base_p95="$(jq -r '.metrics.http_req_duration.values["p(95)"]' "${BASELINE_FILE}")"
	opt_p95="$(jq -r '.metrics.http_req_duration.values["p(95)"]' "${OPTIMIZED_FILE}")"

	if [[ "${base_avg}" != "null" && "${opt_avg}" != "null" ]]; then
		improvement="$(awk -v b="${base_avg}" -v o="${opt_avg}" 'BEGIN { if (b>0) printf "%.2f", ((b-o)/b)*100; else print "0.00" }')"
		echo ""
		echo "Summary (http_req_duration):"
		echo "  Baseline avg:  ${base_avg} ms"
		echo "  Optimized avg: ${opt_avg} ms"
		echo "  Baseline p95:  ${base_p95} ms"
		echo "  Optimized p95: ${opt_p95} ms"
		echo "  Improvement:   ${improvement}% faster (avg)"
	fi
else
	echo ""
	echo "jq not found. Install jq to print automatic comparison from JSON artifacts."
fi
