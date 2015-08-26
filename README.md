# Authentication Service Tester

This framework is used to test the REST API endpoints for the authentication service.
It uses the Spring boot framework to run HTTP requests (wrapped in a WebClient library),
and JUnit to do assertions.

# Installation

# Usage

If you're using the AuthServiceTester project from your localhost to an SUV, run
the following command at the command prompt:

    ssh -L 12766:localhost:12776 root@<suv_host_name>

where you should replace the <suv_host_name> with your SUV host name. Keep that
window open while you run the project.


# Contributing

# History

0.1.0:




GET /token
Authorization: ID [token]
> {
    "header": {
        "alg": "RS512",
        "kid": "AS04_17_2014_11_24_14_298"
    },
    "body": {
        "iss": "OMS",
        "sub": "devadmin",
        "aud": "wd",
        "exp": 1397726834,
        "iat": 1397726654,
        "auth_time": 1397726654,
        "jti": "1dr5i5fl1s3v07qv82tkd9u3oqpxt8ofxsr3i3ds3a8oiu4vgpe1ozmnc7osnctz4pu6usa0yild06607kfwvgn3h7193cye1i5q2gikvm71fwbmw0lblpysff3ae2qonq9dfobur20e8k5qp3gx8mna0vca9o35ewndfa8uk3hl27bmezepgv6r3mz87h70f6go2xhcuoqki6scc4cq0a3ahxom8m9zqlias86qo1eh15po5jaddjhxd4ojgev7v3rj2169hlg9whlvhuemkfybufdevjsz323uaualygjyf765984l07e5nwt9rof86ghmbo5ej9qnbr8p73yoozg5kzaayrkaz4b91odgnfafm7b7p0026we2tfnpey01xztsliuqcgerq",
        "channel": "UI",
        "auth_type": "Password",
        "sys_acct_typ": "N",
        "tenant": "oms",
        "tokenType": "Identity"
    },
    "signature": "lKmAx7I7MsSvUi_xnX9HPSHdKOPNEG1Y6CkgksdZZws9jokUqUPYqe1fmVihIw_IK85fgMoEIzusPBDG3vjZAEtlaNhR6-3EZGLKSY-ZoiCVORjUetgwAAKP-47p59lL6gvQ6nLDUb3KZ1GL_o5Sdbmebi06J_qlOFmC2WFL_U04KvDF8b13oDSQR_AlnwszEImgfz93O7ihWV5VaflwmfxXT-0yCsxnEg5J6evSmbbZuYe7Qs5cAtdw0voV24mY"
}


POST /token/extend
{    
    "token": "[token]",
    "context": "[context]",
    "client": "oms",
    "nonce": "JuxWjYmEwlLNM7P1eIm0",
    "timestamp": "1396362847131",
    "signature": "[client (e.g. OMS) signature]"
}

> [token]


POST /token/renew
{
    "token": "[token]",
    "client": "oms",
    "nonce": "JuxWjYmEwlLNM7P1eIm0",
    "timestamp": "1396362847131",
    "signature": "[client (e.g. OMS) signature]"
}

> [token]


GET /token/verify

Authorization: ID [token]
> {
    "tokenValid": true
    "expirationAt": [timestamp]
}


