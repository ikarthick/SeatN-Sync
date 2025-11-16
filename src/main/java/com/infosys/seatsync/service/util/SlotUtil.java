package com.infosys.seatsync.service.util;

import java.util.Map;

public class SlotUtil {

    // ============================
    // SLOT DEFINITIONS (3h 45m)
    // ============================
    private static final Map<String, String[]> FIXED_SLOTS = Map.of(
            "A", new String[]{"09:00", "12:45"},
            "B", new String[]{"12:45", "16:30"},
            "C", new String[]{"16:30", "20:15"},
            "D", new String[]{"20:15", "23:59"}
    );

    private static final String FULL_DAY_START = "09:00";
    private static final String FULL_DAY_END   = "23:59";

    public static Map<String, String[]> getSlots() {
        return FIXED_SLOTS;
    }

    public static boolean isFullDayBooking(String start, String end) {
        return FULL_DAY_START.equals(start) && FULL_DAY_END.equals(end);
    }

    public static boolean overlaps(String s1, String e1, String s2, String e2) {
        // Overlap rule → s1 < e2 AND s2 < e1
        return s1.compareTo(e2) < 0 && s2.compareTo(e1) < 0;
    }

    public static String getSlotName(String start, String end) {
    	
    	 String normalizedStart = normalizeTime(start);
         String normalizedEnd = normalizeTime(end);

         // Find slot by matching start and end
         return FIXED_SLOTS.entrySet()
                 .stream()
                 .filter(e -> e.getValue()[0].equals(normalizedStart) && e.getValue()[1].equals(normalizedEnd))
                 .map(Map.Entry::getKey)
                 .findFirst()
                 .orElse("");
    }

    private static String normalizeTime(String time) {
        // Replace dot with colon if needed
        time = time.replace('.', ':');

        String[] parts = time.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid time format: " + time);
        }
        return String.format("%02d:%02d", Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }

}
