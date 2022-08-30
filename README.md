# TIP API

## Build

```
docker build . \
  --build-arg MATLAB_RELEASE=r2020a \
  -t tip-api
```

## Configure

### MATLAB

A valid license file (license.lic) is needed to run Matlab. Provide a `license.lic` file in the project root directory.

The MATLAB "host ID" is just the MAC address. Within the container, get it from `/sys/class/net/eth0/address`

### Environment variables

Provide environment variables inline or via a docker environment file:

```
# environment
REDCAP_URL=https://redcap.example.edu/api/
REDCAP_TOKEN=putyourredcaptokenhere
```

## Run

```
docker run --rm \
  -p 8443:8443 \
  --env-file environment \
  --mount type=bind,source="$(pwd)"/license.lic,target=/opt/matlab/licenses/license.lic \
  --mount type=bind,source="$(pwd)"/keystore.p12,target=/app/keystore.p12 \
  tip-api
```
