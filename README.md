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