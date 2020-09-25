#include <iostream>

using namespace std;

int main()
{
     int x;
     unsigned p;

     int power(int x,unsigned p);
     cin>>x>>p;
     cout<< power(x,p);

    return 0;
}
// возведение в степень
int power(int x, unsigned p)
{
     int answer = 1;
     if (p==0) answer = 1;
     else for (int i=0; i<p;i++)
     {
          answer*=x;
     }
     return answer;
}
