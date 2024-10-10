package com.example.server.services;

import com.example.server.dtos.request.JsoupRequest;
import com.example.server.dtos.response.JsoupResponse;

import java.io.IOException;
import java.util.Set;

public interface ScraperService {
    Set<JsoupResponse> getModel() throws IOException;
}
