// RUN: %tool "%s" > "%t"
// RUN: %diff %CORRECT "%t"

int foo() {

    int x;
    int y;
    x = 501;
    y = 0;
    int z;
    z = x / y;
    assert z == 501;
    return 0;

}
