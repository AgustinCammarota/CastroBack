package com.cursoejemplo.backend.apirest.auth;

public class OpenSsl {

    public static final String RSA_PUBLICA = "-----BEGIN PUBLIC KEY-----\n" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA7EBhZikqpdTcm9XvYjMx\n" +
            "/GqRYHZJr5974zYIAOIsVTKMdl+cyJ7P8IqhVf/GYyCJMwWCinx1ZuzXXt/B19Nn\n" +
            "btXwaIHRssp3HKp3basv15UoTvpjmZVlt3yI9WGajFm0OiYpY/LHCiOdfxmDSdcD\n" +
            "r0xmypr4MuSO9zxdMby2GXBUi+trs/z8SchmU5zWybMjlcOtBALaEiuOlU4OK2uM\n" +
            "L1FAZu3x2nE29suGJfmEqWMffhexSJWjOWJeKc7MVvKeHcKNhmzjZdSzKshyC7B6\n" +
            "+Ny6xDBZ18qvxsPsF3WE5ykYy95nVoBlfhpNtbdb6f9QeqAQPDI/rFkIcTp3M8Se\n" +
            "RwIDAQAB\n" +
            "-----END PUBLIC KEY-----";

    public static final String RSA_PRIVADA = "-----BEGIN RSA PRIVATE KEY-----\n" +
            "MIIEpAIBAAKCAQEA7EBhZikqpdTcm9XvYjMx/GqRYHZJr5974zYIAOIsVTKMdl+c\n" +
            "yJ7P8IqhVf/GYyCJMwWCinx1ZuzXXt/B19NnbtXwaIHRssp3HKp3basv15UoTvpj\n" +
            "mZVlt3yI9WGajFm0OiYpY/LHCiOdfxmDSdcDr0xmypr4MuSO9zxdMby2GXBUi+tr\n" +
            "s/z8SchmU5zWybMjlcOtBALaEiuOlU4OK2uML1FAZu3x2nE29suGJfmEqWMffhex\n" +
            "SJWjOWJeKc7MVvKeHcKNhmzjZdSzKshyC7B6+Ny6xDBZ18qvxsPsF3WE5ykYy95n\n" +
            "VoBlfhpNtbdb6f9QeqAQPDI/rFkIcTp3M8SeRwIDAQABAoIBAAb2Aqiq79nBAtsI\n" +
            "m6wODBv+29e2xIcLMPBJ6ER2ZY4BFkhqmskCfOf+fbB1/9dt3v0E3YrNDfyBX/so\n" +
            "pqKAVRzb6uB5ar9U3duXXjt5/sfgWjaxg/vDfiLA5ZwtSwt1nSzVcQTD4391+y8C\n" +
            "hm7HihFBNAomNMfzGg9I1QohIFBxg1zLJt0kvu+28EuWdtpKLDG+MLBfvA+ggN5H\n" +
            "IhkAn7dGffEk3kMBdm/8iAjuECEPF4bPapfXG4+LmCDC0FKTRmDLfDunHF3zLZLC\n" +
            "rYNPJX/POvnNrS0JNKNttqjONouI+Gk6Irxmgfp8nr+3phKOP8WWRyI14jW/gDgC\n" +
            "1VcAqXECgYEA/LhYLIigsBzjxboDWHpwZUih75Zy6loHnbp3YJ9mOw+xpNMllWBr\n" +
            "hQ8JpU8gYDjxXOASxMRdpGCOTm2VHKMUDopaLXd79Cy3h6keR03RofAgDf+DL8Pv\n" +
            "Qu9dhOkOUon1h91b48pNmV3JeZmmewBZr/2w9R9SRh74RQnLa289YZ8CgYEA71FR\n" +
            "zPvza8ypO6Q8xBN3cY1hDY9x6sQhOUTlvdXmusxW7bBPqlBuPKaJAFSl1BVWPWTB\n" +
            "Qf4X/fcI3oDXClTeCWbhi0JVjivIyJ1W39bW6zPb/aJA2rQ4iP7cP97gMKyLeD8s\n" +
            "vlbfF9Ob1YXwjvR5s5myKLQBubNVGegn8JyfklkCgYEAh9gfrXCTz5b36KUgf0Lx\n" +
            "f8AXK85XXxmd1m8RFGMadMzu1IabQTxfD1sR+EYcRRzWwPHBpPtYyq8MT6Qf7xDK\n" +
            "CzbbWm1Lt+kBjm6d0/5EnSOSV3PtdJOB3R0FzBfqlvuJe8rFRiDMrY/4aODq2FtL\n" +
            "2oonwNthQkgLdDDbo5hcCkkCgYEAzp7wOEHjxdPa3UKMsGF6Q9Dj1Xhey2nwoWhp\n" +
            "zsAKSIc6GleaQJrWwoiqSl3LGtQdND50WMzP3BZYUvr8LtBdL9LisoBgK7KD1Sl7\n" +
            "IyFzR3jr3A0lX24wTZ91e7Oq7ntao0QwFlocnY62weC1dEeBd94gIuJfhvxdYlO9\n" +
            "gBFJ+GkCgYBilcjfjqGDyOYcZ0lPNq4Lf1mpYRmb8WIndw3LSyRYnqjxphg2BGfO\n" +
            "k+iIZvo2B+lqTCb/8Q4meVPIHfDclj6Cv7t+V4gWojEI15mkHJTP+WoeD4y3Hld6\n" +
            "mBRRr6Y/KIdX0cVwWh0xX6FUQcZ/OHAaJxPr5IctjNHGWbT+v9iQWg==\n" +
            "-----END RSA PRIVATE KEY-----";
}
