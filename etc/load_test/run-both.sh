#!/usr/bin/env bash
#
# run-both.sh
#
# Runs two vegeta attacks IN PARALLEL against the virtual-threads demo:
#   - platform threads  -> http://localhost:9000 (clojure-api,    THREAD_MODE=platform)
#   - virtual  threads  -> http://localhost:9002 (clojure-api-vt, THREAD_MODE=virtual)
#
# Both flows run simultaneously so you can compare their behaviour in Grafana
# (dashboard "Virtual Threads — Loom Demo", uid loom-threads).
#
# Override RATE / DURATION via env vars, e.g.:
#   RATE=40 DURATION=60s ./run-both.sh
#
set -euo pipefail

# --- configuration (overridable via env) -----------------------------------
RATE="${RATE:-20}"
DURATION="${DURATION:-30s}"

# Resolve the directory this script lives in so it can be invoked from anywhere.
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

PLATFORM_TARGETS="${SCRIPT_DIR}/attack-platform.txt"
VIRTUAL_TARGETS="${SCRIPT_DIR}/attack-virtual.txt"

PLATFORM_BIN="${SCRIPT_DIR}/results-platform.bin"
VIRTUAL_BIN="${SCRIPT_DIR}/results-virtual.bin"

# --- preflight checks -------------------------------------------------------
if ! command -v vegeta >/dev/null 2>&1; then
  echo "ERROR: vegeta is not installed or not on PATH." >&2
  echo "       Install it: https://github.com/tsenart/vegeta" >&2
  exit 1
fi

for f in "${PLATFORM_TARGETS}" "${VIRTUAL_TARGETS}"; do
  if [[ ! -f "${f}" ]]; then
    echo "ERROR: targets file not found: ${f}" >&2
    exit 1
  fi
done

echo "Starting parallel vegeta attacks (rate=${RATE}, duration=${DURATION})..."
echo "  platform -> $(cat "${PLATFORM_TARGETS}")"
echo "  virtual  -> $(cat "${VIRTUAL_TARGETS}")"
echo

# --- run both attacks in parallel ------------------------------------------
vegeta attack -targets="${PLATFORM_TARGETS}" -rate="${RATE}" -duration="${DURATION}" \
  | tee "${PLATFORM_BIN}" >/dev/null &
PLATFORM_PID=$!

vegeta attack -targets="${VIRTUAL_TARGETS}" -rate="${RATE}" -duration="${DURATION}" \
  | tee "${VIRTUAL_BIN}" >/dev/null &
VIRTUAL_PID=$!

# Wait for both attacks to finish; surface a failure of either one.
status=0
wait "${PLATFORM_PID}" || status=$?
wait "${VIRTUAL_PID}"  || status=$?

# --- reports ----------------------------------------------------------------
echo
echo "================ PLATFORM (port 9000) ================"
vegeta report "${PLATFORM_BIN}"

echo
echo "================ VIRTUAL  (port 9002) ================"
vegeta report "${VIRTUAL_BIN}"

exit "${status}"
