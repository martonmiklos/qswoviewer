#ifndef SWVINTERRUPTPARSER_H
#define SWVINTERRUPTPARSER_H

#include "swvclient.h"

class SWVClient;

class SWVInterruptParser
{
public:
    SWVInterruptParser(SWVClient *client);
    ~SWVInterruptParser();

private:
    SWVClient *m_client;
};

#endif // SWVINTERRUPTPARSER_H
