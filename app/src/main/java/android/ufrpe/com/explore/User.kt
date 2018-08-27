package android.ufrpe.com.explore

/**
 * Created by Marcelo on 05/08/2018.
 */

class User {
    var email: String? = null
        private set
    var imgUrl: String? = null

    constructor() {

    }

    constructor(email: String) {
        this.email = email
    }

    constructor(email: String, imgUrl: String) {
        this.email = email
        this.imgUrl = imgUrl
    }
}
