package com.aptoide.uploader.apps.network;

import java.io.IOException;

public class GetApksRetryException extends IOException {

  @Override public String getMessage() {
    return "GetApks timer retry error";
  }
}