# 【Android】アプリにログイン機能をつけよう！ for Kotlin

![画像1](/readme-img/001.png)

## 概要
* [ニフクラ mobile backend](https://mbaas.nifcloud.com/) の『会員管理機能』を利用して、Androidアプリにログイン機能を実装したサンプルアプリです
* 簡単な操作ですぐに [ ニフクラ mobile backend ](https://mbaas.nifcloud.com/)の機能を体験いただけます

##  ニフクラ mobile backend って何？？
スマートフォンアプリのバックエンド機能（プッシュ通知・データストア・会員管理・ファイルストア・SNS連携・位置情報検索・スクリプト）が**開発不要**、しかも基本**無料**(注1)で使えるクラウドサービス！

注1：詳しくは[こちら](https://mbaas.nifcloud.com/price.htm)をご覧ください

### ニフクラ mobile backend の会員の認証方法
* ユーザ名・パスワード認証
* メールアドレス・パスワード認証
* SNSアカウントでの認証
* 匿名認証

本サンプルアプリは、ユーザ名・パスワードでの認証方法について説明していきます。

## 動作環境

* Android Studio ver. 3.1
* Android OS ver. 6.0
* Android SDK v3
  - SDK v2系だと動作しないので注意

※上記内容で動作確認をしています。

## 手順
### 1. ニフクラ mobile backend の会員登録・ログインとアプリの新規作成
* 下記リンクから会員登録（無料）をします
  * https://console.mbaas.nifcloud.com/signup
* 登録ができたら下記リンクからログインします
  * https://console.mbaas.nifcloud.com/
* 下図のように「アプリの新規作成」画面が出るのでアプリを作成します
  * 既に mobile backend を利用したことがある方は左上の「＋新しいアプリ」をクリックすると同じ画面が表示されます

![画像3](/readme-img/003.png)

* アプリ作成されるとAPIキー（アプリケーションキーとクライアントキー）が発行されます
* この２種類のAPIキーはこの後ダウンロードするサンプルアプリと ニフクラ mobile backend を紐付けるため、あとで使います。

![画像04](/readme-img/004.png)

* ついでに、この後動作確認で会員情報が保存される場所も確認しておきましょう

![画像05](/readme-img/005.png)

### 2. サンプルプロジェクトのダウンロード
* 下記リンクをクリックしてプロジェクトをダウンロードします
 　* https://github.com/NiFCloud-mbaas/KotlinLoginApp/archive/master.zip
* ダウンロードしたプロジェクトを解凍します
* AndroidStudio を開きます、「Open an existing Android Studio projct」をクリックして解凍したプロジェクトを選択します

![画像05](/readme-img/android_studio.png)

* プロジェクトが開かれます

![MainDesing.png](/readme-img/MainDesing.png)


### 3. SDKの導入（実装済み）

※このサンプルアプリには既にSDKが実装済み（下記手順）となっています。（ver.3.0.0)<br>　最新版をご利用の場合は入れ替えてご利用ください。

* SDKダウンロード
SDKはここ（[SDK リリースページ](https://github.com/NIFCloud-mbaas/ncmb_android/releases)）から取得してください.
  - NCMB.jarファイルがダウンロードします。
* SDKをインポート
  - app/libsフォルダにNCMB.jarをコピーします
* 設定追加
  - app/build.gradleファイルに以下を追加します
```gradle
dependencies {
    compile 'com.google.code.gson:gson:2.3.1'
    compile files('libs/NCMB.jar')
}
```
  - androidManifestの設定
    - <application>タグの直前に以下のpermissionを追加します
```html
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

### 4. APIキーの設定

* AndroidStudio で MainActivity.kt を開きます
  * ディレクトリはデフォルトで「Android」が選択されていますので、「Project」に切り替えてから探してください

![画像09](/readme-img/009.png)

* APIキー（アプリケーションキーとクライアントキー）の設定をします

![画像07](/readme-img/007.png)

* それぞれ `YOUR_APPLICATION_KEY` と `YOUR_CLIENT_KEY` の部分を書き換えます
 * このとき、ダブルクォーテーション（`"`）を消さないように注意してください

### 5. 動作確認

* エミュレーターでアプリをビルドします
  * 失敗する場合は一度「Clean Project」を実行してから再度ビルドしてください

  * 画面に従い新規登録、ログインを実施してみましょう
 
* 【会員の新規登録の場合】
![AccountPattern.png](/readme-img/AccountPattern.png)

* 【既存会員のログインの場合】
![LoginPattern.png](/readme-img/LoginPattern.png)

* ニフクラ mobile backend 側を確認すると、会員管理データが保存されたことを確認できます

![画像08](/readme-img/008.png)


## コード説明

### 必要なライブラリーのインポート

```kotlin
import com.nifcloud.mbaas.core.NCMB
import com.nifcloud.mbaas.core.NCMBUser
import com.nifcloud.mbaas.core.NCMBException
```

### SDKの初期化

* MainActivity.kt の `OnCreate` メソッドに実装しています

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    <省略>
    //**************** APIキーの設定とSDKの初期化 **********************
    NCMB.initialize(this, "YOUR_APPLICATION_KEY", "YOUR_CLIENT_KEY")
}
```

### 新規登録

* SDKが提供する `NCMBUser` クラスが会員管理を操作するためのクラスです
* このクラスが提供する `signUpInBackground` メソッドを利用し、新規登録を行います（非同期処理）
* 設定したユーザ名(userName)とパスワード(password)で会員登録を行います

```kotlin
fun signup() {
    <省略>
    // TODO: Implement your own signup logic here.
    //NCMBUserのインスタンスを作成
    val user = NCMBUser()
    //ユーザ名を設定
    user.userName = name
    //パスワードを設定
    user.setPassword(password)
    //設定したユーザ名とパスワードで会員登録を行う
    user.signUpInBackground { e ->
        if (e != null) {
            //会員登録時にエラーが発生した場合の処理
            onSignupFailed()
        } else {
            android.os.Handler().postDelayed(
                {
                    // On complete call either onSignupSuccess or onSignupFailed
                    // depending on success
                    onSignupSuccess()
                    // onSignupFailed();
                    progressDialog.dismiss()
                }, 3000)
        }
    }
}
```

### 既存会員のログイン

* `NCMBUser` クラスが提供する `loginInBackground` メソッドを利用し、ログインします（非同期処理）
* 設定したユーザ名(userName)とパスワード(password)でログインを行いま

```kotlin
fun login() {
<省略>
    // TODO: Implement your own authentication logic here.
    //ユーザ名とパスワードを指定してログインを実行
    try {
        NCMBUser.loginInBackground(name, password) { user, e ->
            if (e != null) {
                //エラー時の処理
                onLoginFailed()
            } else {
                android.os.Handler().postDelayed(
                        {
                            // On complete call either onLoginSuccess or onLoginFailed
                            onLoginSuccess()
                            // onLoginFailed();
                            progressDialog.dismiss()
                        }, 3000)
            }
        }
    } catch (e: NCMBException) {
        e.printStackTrace()
    }

}
```

### ログアウト

* `NCMBUser` クラスが提供する `loginInBackground` メソッドを利用し、ログアウトを行います（非同期処理）

```kotlin
NCMBUser.logoutInBackground { e ->
    if (e != null) {
        //エラー時の処理
    }
}
```

## 参考

データ保存・データ検索・会員管理・プッシュ通知などの機能を実装したい場合は、以下のドキュメント（Android for Java）もご参照ください。

* [ドキュメント](https://mbaas.nifcloud.com/doc/current/)
  * [データストア](https://mbaas.nifcloud.com/doc/current/datastore/basic_usage_android.html)
  * [会員管理](https://mbaas.nifcloud.com/doc/current/user/basic_usage_android.html)
  * [プッシュ通知](https://mbaas.nifcloud.com/doc/current/push/basic_usage_android.html)

# Contributing

*    Fork it!
*    Create your feature branch: git checkout -b my-new-feature
*    Commit your changes: git commit -am 'Add some feature'
*    Push to the branch: git push origin my-new-feature
*    Submit a pull request :D

# License

    MITライセンス
    NIFCLOUD mobile backendのAndroid SDKのライセンス
