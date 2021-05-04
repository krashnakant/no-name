package com.vision.app.exceptions

import java.lang.RuntimeException

class UserNotFound(s: String) : RuntimeException(s)
