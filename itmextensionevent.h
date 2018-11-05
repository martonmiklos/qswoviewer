#ifndef ITMEXTENSIONEVENT_H
#define ITMEXTENSIONEVENT_H

#include "itmevent.h"

class ITMExtensionEvent : public ITMEvent
{
public:
    ITMExtensionEvent(quint8 page);
};

#endif // ITMEXTENSIONEVENT_H
