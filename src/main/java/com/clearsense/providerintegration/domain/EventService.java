package com.clearsense.providerintegration.domain;

import io.vavr.control.Either;

import javax.xml.transform.ErrorListener;

public interface EventService {

    Either<ErrorListener, String> cloneRepo(final String repoUrl);
}
