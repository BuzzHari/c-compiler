#include <stdio.h>

int main() {
    int a = 5 * 20;
    int b = a + 30;
    int c = a + b;
    int d = 35;
    
    while(a > 100)
    {
        b = c + d;
        a = a - 1;
    }
    
    return 0;
}
