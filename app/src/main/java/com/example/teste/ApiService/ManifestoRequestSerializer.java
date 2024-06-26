package com.example.teste.ApiService;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class ManifestoRequestSerializer implements JsonSerializer<ManifestoRequest> {

    @Override
    public JsonElement serialize(ManifestoRequest src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", src.getId());
        jsonObject.addProperty("telefone", src.getTelefone());
        jsonObject.addProperty("manifesto", src.getManifesto());
        return jsonObject;
    }
}
