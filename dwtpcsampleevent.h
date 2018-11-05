#ifndef DWTPCSAMPLEEVENT_H
#define DWTPCSAMPLEEVENT_H

#include "itmevent.h"

class DWTPCSampleEvent : public ITMEvent
{
public:
    DWTPCSampleEvent(byte type_, long pcAddress);

    long pcAddress() const;

private:
    byte m_type;
    long m_pcAddress;
};

#endif // DWTPCSAMPLEEVENT_H
