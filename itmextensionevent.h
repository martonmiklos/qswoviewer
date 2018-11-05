#ifndef ITMEXTENSIONEVENT_H
#define ITMEXTENSIONEVENT_H

#include "itmevent.h"

class ITMExtensionEvent : public ITMEvent
{
public:
    ITMExtensionEvent(byte page);
};

#endif // ITMEXTENSIONEVENT_H
