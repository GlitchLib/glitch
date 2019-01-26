package glitch.chat.object.irc;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableRangeSet;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Range;
import glitch.api.objects.enums.UserType;
import glitch.api.objects.json.Badge;

import javax.annotation.Nullable;
import java.awt.*;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Tags are be part Immutable {@link java.util.Map Map} of {@link java.lang.String Strings}.
 * Data receives of the IRC are be parsed into this object.
 *
 * @author Damian Staszewski
 * @see java.util.Map
 * @see java.time.Instant
 * @see Badge
 * @see Emote
 * @see com.google.common.collect.ImmutableList
 * @see com.google.common.collect.ImmutableSet
 * @since 0.2.0
 */
public class Tags implements Map<String, String> {
    private static final String[] booleanKeys = new String[] {"turbo", "mod", "subscriber", "emote-only", "r9k", "subs-only"};

    private final Map<String, String> tags;

    private Tags(Map<String, String> tags) {
        this.tags = Collections.unmodifiableMap(tags);
    }

    public static Tags of(Map<String, String> tags) {
        return new Tags(Collections.unmodifiableMap(tags));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return tags.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return tags.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsKey(Object key) {
        return tags.containsKey(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsValue(Object value) {
        return tags.containsValue(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get(Object key) {
        return tags.get(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String put(String key, String value) {
        return tags.put(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String remove(Object key) {
        return tags.remove(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putAll(Map<? extends String, ? extends String> m) {
        tags.putAll(m);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        tags.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> keySet() {
        return tags.keySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> values() {
        return tags.values();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Entry<String, String>> entrySet() {
        return tags.entrySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return tags.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        return tags.equals(obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return tags.toString();
    }

    /**
     * Formatting tag value into {@link java.lang.Boolean}
     *
     * @param key key with which the specified value is to be associated
     * @return the value to which the specified key is mapped, or return {@code false} if this map contains no mapping for the key
     */
    public Boolean getBoolean(String key) {
        return Arrays.asList(booleanKeys).contains(key) && get(key).matches("[0-1]") && get(key).equals("1");
    }

    /**
     * Formatting tag value into {@link java.lang.Integer}
     *
     * @param key key with which the specified value is to be associated
     * @return the value to which the specified key is mapped, or return {@code 0} if this map contains no mapping for the key
     */
    public Integer getInteger(String key) {
        return Integer.parseInt(getOrDefault(key, "0"));
    }

    /**
     * Formatting tag value into {@link java.lang.Boolean}
     *
     * @param key key with which the specified value is to be associated
     * @return the value to which the specified key is mapped, or return {@code 0L} if this map contains no mapping for the key
     */
    public long getLong(String key) {
        return Long.parseLong(getOrDefault(key, "0"));
    }

    /**
     * Getting {@link Badge badges} if {@code badge} key is exist.
     *
     * @return Getting existing {@link Badge badges}
     */
    public ImmutableSet<Badge> getBadges() {
        return ImmutableSet.copyOf(entrySet().stream().filter(entry -> entry.getKey().equals("badges"))
                .map(Entry::getValue).findFirst()
                .map(value -> Arrays.stream(value.split(",")))
                .map(badges -> badges.map(badge -> new Badge(badge.split("/")[0], Integer.parseInt(badge.split("/")[1])))
                        .collect(Collectors.toSet())).orElse(Collections.emptySet()));
    }

    /**
     * Getting {@link Emote emote indexes} if {@code emotes} key is exist.
     *
     * @return Getting existing {@link Emote emote indexes}
     */
    public ImmutableList<Emote> getEmotes() {
        return ImmutableList.copyOf(entrySet().stream().filter(entry -> entry.getKey().equals("emotes"))
                .map(Entry::getValue).findFirst().map(value -> Arrays.stream(value.split("/")))
                .map(emotes -> emotes.map(emote -> {
                    String[] emoteIndexes = emote.split(":");
                    Set<Range<Integer>> indexes = Arrays.stream(emoteIndexes[1].split(","))
                            .map(index -> {
                                String[] part = index.split("-");
                                return Range.closed(Integer.parseInt(part[0]), Integer.parseInt(part[1]));
                            }).collect(Collectors.toSet());

                    return new Emote(Integer.parseInt(emoteIndexes[0]), ImmutableRangeSet.copyOf(indexes));
                }).collect(Collectors.toList())).orElse(Collections.emptyList()));
    }

    /**
     * Getting Emote sets into {@link java.lang.Integer integer} if {@code emote-sets} key is exist.
     *
     * @return Getting existing emote sets into {@link java.lang.Integer integer}
     */
    public ImmutableList<Integer> getEmoteSets() {
        return ImmutableList.copyOf(entrySet().stream().filter(entry -> entry.getKey().equals("emote-sets"))
                .map(Entry::getValue).findFirst()
                .map(value -> Arrays.stream(value.split(",")).map(Integer::parseInt).collect(Collectors.toList()))
                .orElse(Collections.emptyList()));
    }

    /**
     * Getting {@link java.time.Instant timestamp} if {@code tmi-sent-ts} key is exist.
     *
     * @return Getting existing {@link java.time.Instant timestamp} otherwise returns {@link java.time.Instant#now() current time}
     */
    public Instant getSentTimestamp() {
        return entrySet().stream().filter(entry -> entry.getKey().equals("tmi-sent-ts"))
                .map(Entry::getValue).findFirst().map(timestamp -> new Date(Integer.parseInt(timestamp)).toInstant()).orElse(Instant.now());
    }

    /**
     * Getting broadcast language formatting into {@link java.util.Locale {@code Locale}} if {@code broadcast-lang} key is exist.
     *
     * @return Getting existing broadcast language formatting into {@link java.util.Locale {@code Locale}} otherwise returns {@code null}
     */
    @Nullable
    public Locale getBroadcasterLanguage() {
        return entrySet().stream().filter(entry -> entry.getKey().equals("broadcast-lang"))
                .map(Entry::getValue).findFirst()
                .map(Locale::forLanguageTag)
                .orElse(null);
    }

    /**
     * Getting {@link UserType user type} if {@code user-type} key is exist.
     *
     * @return Getting existing {@link UserType user type} otherwise returns {@code null}
     */
    @Nullable
    public UserType getUserType() {
        return entrySet().stream().filter(entry -> entry.getKey().equals("user-type"))
                .map(Entry::getValue).findFirst()
                .map(UserType::from)
                .orElse(null);
    }

    /**
     * Getting {@link java.awt.Color user color} if {@code color} key is exist.
     *
     * @return Getting existing {@link UserType user color} otherwise returns {@code null}
     */
    @Nullable
    public Color getColor() {
        return entrySet().stream().filter(entry -> entry.getKey().equals("color"))
                .map(Entry::getValue).findFirst()
                .map(Color::decode)
                .orElse(null);
    }
}