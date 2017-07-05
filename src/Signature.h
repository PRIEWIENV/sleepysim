#ifndef BLOCKCHAIN_SIGNATURE_H
#define BLOCKCHAIN_SIGNATURE_H

#include "header.h"
#include "Message.h"

class Signature
{
private:
    std::vector <std::string> secret_key_table, public_key_table;
public:
    Signature(int node_count);
    ~Signature();
    std::string generate_signature(int node_id, Message message);
    bool check_signature(int node_id, std::string signature);
    std::string get_secret_key(int node_id);
    std::string get_public_key(int node_id);
};


#endif //BLOCKCHAIN_SIGNATURE_H
