package com.sdp.movemeet.Activity;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.sdp.movemeet.Activity.ActivitySerializer.ACTIVITY_KEY;
import static com.sdp.movemeet.Activity.ActivitySerializer.ADDRESS_KEY;
import static com.sdp.movemeet.Activity.ActivitySerializer.CREATION_KEY;
import static com.sdp.movemeet.Activity.ActivitySerializer.DATE_KEY;
import static com.sdp.movemeet.Activity.ActivitySerializer.DESC_KEY;
import static com.sdp.movemeet.Activity.ActivitySerializer.DURATION_KEY;
import static com.sdp.movemeet.Activity.ActivitySerializer.LATITUDE_KEY;
import static com.sdp.movemeet.Activity.ActivitySerializer.LONGITUDE_KEY;
import static com.sdp.movemeet.Activity.ActivitySerializer.NPART_KEY;
import static com.sdp.movemeet.Activity.ActivitySerializer.ORGANIZER_KEY;
import static com.sdp.movemeet.Activity.ActivitySerializer.PARTICIPANTS_KEY;
import static com.sdp.movemeet.Activity.ActivitySerializer.SPORT_KEY;
import static com.sdp.movemeet.Activity.ActivitySerializer.TITLE_KEY;
import static com.sdp.movemeet.Activity.ActivitySerializer.deserialize;
import static com.sdp.movemeet.Activity.ActivitySerializer.serialize;
import static com.sdp.movemeet.Activity.ActivityTest.createFakeActivity;

public class ActivitySerializerTest {

    @Test
    public void serializerWorks() {
        Activity fakeActivity = createFakeActivity();

        Map<String, Object> s = serialize(fakeActivity);

        assert(s.get(ACTIVITY_KEY).equals(fakeActivity.getActivityId()));
        assert(s.get(ORGANIZER_KEY).equals(fakeActivity.getOrganizerId()));
        assert(s.get(TITLE_KEY).equals(fakeActivity.getTitle()));

        assert(s.get(NPART_KEY).equals(fakeActivity.getNumberParticipant()));
        assert(s.get(PARTICIPANTS_KEY).equals(fakeActivity.getParticipantId()));

        assert(s.get(LONGITUDE_KEY).equals(fakeActivity.getLongitude()));
        assert(s.get(LATITUDE_KEY).equals(fakeActivity.getLatitude()));

        assert(s.get(DATE_KEY).equals(fakeActivity.getDate()));
        assert(s.get(DESC_KEY).equals(fakeActivity.getDescription()));
        assert(s.get(DURATION_KEY).equals(fakeActivity.getDuration()));
        assert(s.get(SPORT_KEY).equals(fakeActivity.getSport()));
        assert(s.get(ADDRESS_KEY).equals(fakeActivity.getAddress()));
    }

    @Test
    public void deserializerWorks() {
        Activity fakeActivity = createFakeActivity();

        Map<String, Object> data = new HashMap<String, Object>();
        data.put(ACTIVITY_KEY, fakeActivity.getActivityId());
        data.put(ORGANIZER_KEY, fakeActivity.getOrganizerId());
        data.put(TITLE_KEY, fakeActivity.getTitle());

        data.put(LONGITUDE_KEY, fakeActivity.getLongitude());
        data.put(LATITUDE_KEY, fakeActivity.getLatitude());

        data.put(NPART_KEY, fakeActivity.getNumberParticipant());
        data.put(PARTICIPANTS_KEY, fakeActivity.getParticipantId());

        data.put(DESC_KEY, fakeActivity.getDescription());
        data.put(DATE_KEY, fakeActivity.getDate());
        data.put(DURATION_KEY, fakeActivity.getDuration());
        data.put(SPORT_KEY, fakeActivity.getSport());
        data.put(ADDRESS_KEY, fakeActivity.getAddress());
        data.put(CREATION_KEY, fakeActivity.getCreatedAt());

        Activity deserialized = deserialize(data);

        assert(fakeActivity.equals(deserialized));
    }

}
