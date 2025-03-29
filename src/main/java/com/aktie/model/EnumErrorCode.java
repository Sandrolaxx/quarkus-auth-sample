package com.aktie.model;

public enum EnumErrorCode {

    EMAIL_EM_USO("001", "E-mail já em uso!", 400),
    ERRO_LOGIN("002", "Não foi possível realizar o login. Verifique os dados informados.", 422),
    USUARIO_NAO_ENCONTRADO("003", "Usuário já cadastrado.", 400),
    EMAIL_INVALIDO("004", "E-mail informado inválido.", 400),
    ERRO_LOGIN_INTERNO("005", "Não foi possível realizar o login. Verifique os dados informados.", 500);

    private final String key;

    private final String erro;

    private final int httpStatus;

    private EnumErrorCode(String key, String error, int httpStatus) {
        this.key = key;
        this.erro = error;
        this.httpStatus = httpStatus;
    }

    public String getKey() {
        return key;
    }

    public String getErro() {
        return erro;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public static EnumErrorCode parseByKey(String key) {
        if (key != null && !key.trim().isEmpty()) {
            for (var value : EnumErrorCode.values()) {
                if (value.getKey().equalsIgnoreCase(key)) {
                    return value;
                }
            }
        }

        return null;
    }

}
