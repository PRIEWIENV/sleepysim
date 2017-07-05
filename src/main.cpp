#include "header.h"
#include "Framework.h"

bool detect_inconsistency()
{
    return false;
}

std::vector <int> elect_leader(int current_round)
{
    return std::vector <int> ();
}

void run()
{
    int n = 0;//number of nodes
    int adversary_count = 0;
    Signature signature(n);
    Framework framework(n, adversary_count, &signature);
    for (int current_round = 0; !detect_inconsistency(); current_round ++)
    {

    }
}

int main()
{
    return 0;
}
