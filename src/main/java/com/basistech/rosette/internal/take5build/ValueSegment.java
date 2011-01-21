/******************************************************************************
 ** This data and information is proprietary to, and a valuable trade secret
 ** of, Basis Technology Corp.  It is given in confidence by Basis Technology
 ** and may only be used as permitted under the license agreement under which
 ** it has been distributed, and in no other way.
 **
 ** Copyright (c) 2010 Basis Technology Corporation All rights reserved.
 **
 ** The technical data and information provided herein are provided with
 ** `limited rights', and the computer software provided herein is provided
 ** with `restricted rights' as those terms are defined in DAR and ASPR
 ** 7-104.9(a).
 ******************************************************************************/

package com.basistech.rosette.internal.take5build;

import java.io.IOException;

class ValueSegment extends BufferedSegment {

    // Note that the constructor has the side effect of initializing the
    // address slot in all of the values.
    ValueSegment(Take5Builder builder, String description) {
        super(builder, description);
        for (Value v : builder.valueRegistry) {
            while (v != null) {
                v.address = reserveChunk(v.length, v.alignment);
                v = v.next;
            }
        }
    }

    void writeData() throws IOException {
        for (Value v : builder.valueRegistry) {
            while (v != null) {
                allocateChunk(v.length, v.alignment);
                assert address == v.address;
                byteBuffer.position(offset);
                byteBuffer.put(v.data, v.offset, v.length);
                v = v.next;
            }
            flushChunk();
        }
    }
}