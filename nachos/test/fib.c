/*
 *  Extra testing
 *  Author: dongk@union.edu
 *  Compute the n th fibonacci given the first 2
 *  If the value exceed 32 unsigned bit, wrap around to 0 (answer mod (2^32 - 1))
 */

#include "stdlib.h"

#define BUFFERSIZE	30

int mat_power(unsigned int* res, unsigned int* accum, unsigned int n){
    while(n > 0){
        int a0 = accum[0], a1 = accum[1], a2 = accum[2], a3 = accum[3];
        if(n & 1){
            // res *= accum
            int r0 = res[0], r1 = res[1], r2 = res[2], r3 = res[3];
            res[0] = a0 * r0 + a1 * r2;
            res[1] = a0 * r1 + a1 * r3;
            res[2] = a2 * r0 + a3 * r2;
            res[3] = a2 * r1 + a3 * r3;
        }
       // accum * acumm
       accum[0] = a0 * a0 + a1 * a2;
       accum[1] = a0 * a1 + a1 * a3;
       accum[2] = a2 * a0 + a3 * a2;
       accum[3] = a2 * a1 + a3 * a3;
       n = n >> 1;
    }

}


int main(int argc, char *argv[]) {
    char buffer[BUFFERSIZE];
    unsigned int f1, f2, n;

    printf("f(1): ");
    readline(buffer, BUFFERSIZE);
    f1 = atoi(buffer);


    printf("f(2): ");
    readline(buffer, BUFFERSIZE);
    f2 = atoi(buffer);

    printf("n: ");
    readline(buffer, BUFFERSIZE);
    n = atoi(buffer);

    if (n <= 0){
        printf("invalid input");
        exit(0);
    }

    unsigned int answer;
    if (n == 1) answer = f1;
    else if (n == 2) answer = f2;
    else {
        unsigned int pow[4] = {1, 0, 0, 1};
        unsigned int accum[4] = {1, 1, 1, 0};
        mat_power(pow, accum, n - 2);
        answer = pow[0] * f1 + pow[1] * f2;
    }
    printf("f(%d): %d\n", n, answer);
    return 0;
}

