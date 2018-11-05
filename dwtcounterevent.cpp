#include "dwtcounterevent.h"

#include "itmevent.h"

DWTCounterEvent::DWTCounterEvent(quint8 counters) :
    ITMEvent(),
    m_counters(counters)
{

}
