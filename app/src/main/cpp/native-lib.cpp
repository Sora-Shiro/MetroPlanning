#include <jni.h>
#include <string>

extern "C"
jstring
Java_com_sorashiro_metroplanning_jni_CoreData_stringFromJNI(
        JNIEnv *env,
        jobject object) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
jstring
Java_com_sorashiro_metroplanning_jni_CoreData_getLevelData(
        JNIEnv *env, jobject object, jint level) {

    std::string result = "";

    switch (level) {
        case 1:
            result += "6 6 20 100,";
            result += "2 ";
            result += "0 0 black down 1000 50 0 4 black down 1000 50,";
            result += "2 ";
            result += "2 1 black 100 4 3 black 0,";
            result += "4 ";
            result += "1 0 grey right 3 0 grey right 3 4 grey left 1 4 grey left";
            break;
        case 2:
            result += "6 6 30 300,";
            result += "2 ";
            result += "2 2 black up 1000 50 4 2 black down 1000 50,";
            result += "3 ";
            result += "1 3 black 100 3 1 black 100 4 4 black 100,";
            result += "5 ";
            result += "1 0 grey left 3 0 grey down 1 2 grey down 5 2 grey right 5 4 grey up";
            break;
        case 3:
            result += "6 6 60 450,";
            result += "2 ";
            result += "1 0 black right 1000 50 2 1 green right 1000 50,";
            result += "4 ";
            result += "0 2 black 100 3 0 green 100 3 3 green 100 4 0 black 150,";
            result += "6 ";
            result += "1 1 grey down 1 4 grey left 2 0 grey right 2 5 grey left 5 1 grey right 5 5 grey up";
            break;
        case 4:
            result += "6 6 35 400,";
            result += "2 ";
            result += "0 1 green down 1000 50 2 2 blue right 1000 50,";
            result += "5 ";
            result += "1 4 blue 100 2 0 blue 100 3 0 green 100 3 4 blue 100 5 4 green 0,";
            result += "4 ";
            result += "2 1 grey right 2 5 grey left 4 1 grey right 4 5 grey up";
            break;

        default:
            break;
    }

    return env->NewStringUTF(result.c_str());
}

extern "C"
jint
Java_com_sorashiro_metroplanning_jni_CoreData_getLevels(
        JNIEnv *env, jobject object) {

    return 4;
}
