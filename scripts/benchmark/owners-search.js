import http from "k6/http";
import { check, sleep } from "k6";

const BASE_URL = __ENV.BASE_URL || "http://localhost:8080";
const ENDPOINT = __ENV.ENDPOINT || "/owners";
const LAST_NAME = __ENV.LAST_NAME || "";
const PAGE = __ENV.PAGE || "1";
const THINK_TIME = Number(__ENV.THINK_TIME || "0.2");

const vus = Number(__ENV.VUS || "20");
const duration = __ENV.DURATION || "30s";

export const options = {
  vus,
  duration,
  thresholds: {
    http_req_failed: ["rate<0.01"],
    http_req_duration: ["p(95)<1000"],
  },
};

function buildUrl() {
  const query = `page=${encodeURIComponent(PAGE)}&lastName=${encodeURIComponent(LAST_NAME)}`;
  return `${BASE_URL}${ENDPOINT}?${query}`;
}

export default function () {
  const url = buildUrl();
  const res = http.get(url, {
    tags: {
      endpoint: ENDPOINT,
      scenario: "owner_search",
    },
  });

  check(res, {
    "status is 2xx/3xx": (r) => r.status >= 200 && r.status < 400,
  });

  sleep(THINK_TIME);
}
