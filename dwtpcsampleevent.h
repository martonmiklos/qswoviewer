#ifndef DWTPCSAMPLEEVENT_H
#define DWTPCSAMPLEEVENT_H

#include "itmevent.h"

class DWTPCSampleEvent : public ITMEvent
{
public:
    DWTPCSampleEvent(quint8 type_, long pcAddress);

    long pcAddress() const;

private:
    quint8 m_type;
    long m_pcAddress;
};

#endif // DWTPCSAMPLEEVENT_H
