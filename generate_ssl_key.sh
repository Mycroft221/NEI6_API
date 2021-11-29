sudo openssl pkcs12 -export -in /etc/letsencrypt/live/nei6.ddns.net/fullchain.pem \
                 -inkey /etc/letsencrypt/live/nei6.ddns.net/privkey.pem \
                 -out src/main/resources/keystore.p12 \
                 -name tomcat \
                 -CAfile /etc/letsencrypt/live/nei6.ddns.net/chain.pem \
                 -caname root \
                 -password pass:sherlock
