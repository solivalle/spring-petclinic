#!/usr/bin/env bash
set -euo pipefail

if [[ "${SKIP_SECRET_SCAN:-}" == "1" ]]; then
  exit 0
fi

if ! git rev-parse --is-inside-work-tree >/dev/null 2>&1; then
  exit 0
fi

staged_files="$(git diff --cached --name-only --diff-filter=ACMR)"
if [[ -z "${staged_files}" ]]; then
  exit 0
fi

# Common high-signal patterns for leaked credentials.
known_secret_regex='AKIA[0-9A-Z]{16}|ASIA[0-9A-Z]{16}|AIza[0-9A-Za-z_-]{35}|gh[pousr]_[A-Za-z0-9]{36,255}|xox[baprs]-[A-Za-z0-9-]{10,}|sk_(live|test)_[0-9A-Za-z]{16,}|-----BEGIN (RSA|DSA|EC|OPENSSH|PGP) PRIVATE KEY-----'
generic_secret_regex='(api[-_ ]?key|secret|token|password|passwd|private[-_ ]?key|client[-_ ]?secret)[[:space:]]*[:=][[:space:]]*["'"'"']?[^"'"'"'[:space:]]{1,}'
placeholder_regex='(example|sample|dummy|test|fake|placeholder|changeme|your[_ -]?key|\$\{[A-Za-z0-9_.-]+\}|<[^>]+>)'

has_findings=0

while IFS= read -r file; do
  [[ -z "$file" ]] && continue

  numstat_line="$(git diff --cached --numstat -- "$file" | head -n 1)"
  added_count="$(printf '%s' "$numstat_line" | awk '{print $1}')"
  if [[ "${added_count}" == "-" ]]; then
    continue
  fi

  added_lines="$(git diff --cached --no-color --unified=0 -- "$file" \
    | grep -E '^\+[^+]' \
    | sed 's/^+//' || true)"

  [[ -z "$added_lines" ]] && continue

  known_matches="$(printf '%s\n' "$added_lines" | grep -nE "$known_secret_regex" || true)"
  generic_matches="$(printf '%s\n' "$added_lines" | grep -nEi "$generic_secret_regex" | grep -Eiv "$placeholder_regex" || true)"

  if [[ -n "$known_matches" || -n "$generic_matches" ]]; then
    has_findings=1
    echo "Potential secret detected in staged file: $file"
    if [[ -n "$known_matches" ]]; then
      printf '%s\n' "$known_matches" | head -n 3 | sed 's/^/  line /'
    fi
    if [[ -n "$generic_matches" ]]; then
      printf '%s\n' "$generic_matches" | head -n 3 | sed 's/^/  line /'
    fi
  fi
done <<< "$staged_files"

if [[ "$has_findings" -ne 0 ]]; then
  echo
  echo "Commit blocked by secret scanner."
  echo "Remove the secret, move it to env vars/secrets manager, and retry."
  echo "Temporary bypass: SKIP_SECRET_SCAN=1 git commit ..."
  exit 1
fi

exit 0
