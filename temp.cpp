#include <bits/stdc++.h>
using namespace std;
typedef long long ll;
typedef pair<int, int> pii;
typedef pair<ll, ll> pll;
typedef unsigned long long ull;
typedef pair<double, double> pdd;
typedef vector<vector<ll>> vll;
typedef tuple <ll, ll, ll, ll, ll> tl;
typedef tuple<int, int, int> ti;
int dx[4] = { -1, 0, 1, 0 };
int dy[4] = { 0, 1, 0, -1 };
ll gcd(ll a, ll b) { for (; b; a %= b, swap(a, b)); return a; }
int num[4] = { 1, 5, 10, 50 };
set<int> st;
int n, prv[5];

void dfs(int idx, int cnt)
{
    if (cnt == n)
    {
        int res = 0;
        for (int i = 0; i < n; i++)
            res += prv[i];

        st.insert(res);
        return;
    }

    for (int i = idx; i < 4; i++)
    {
        prv[cnt] = num[i];
        dfs(i, cnt + 1);
    }
}

int main(void)
{
    ios::sync_with_stdio(0);
    cin.tie(0), cout.tie(0);
    cin >> n;
    dfs(0, 0);

    cout << st.size();
}