package streaming.engine.service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import streaming.engine.enums.data.Genre;

@Service
public class GenreGraph {

    private final Map<Genre, List<Genre>> mp = new EnumMap<>(Genre.class);
    private final Genre[] genres = new Genre[]{Genre.ACTION, Genre.ADVENTURE, Genre.DRAMA, Genre.FANTASY, Genre.GORR, Genre.ISEKAI, Genre.KIDS, Genre.LOVE, Genre.MURDER, Genre.MYSTERY, Genre.PSYCHOLOGICAL, Genre.SCHOOL, Genre.SOFT, Genre.THRILL};
    private final Map<Genre, Integer> indexMap = new LinkedHashMap<>();
    private final int TOTAL = genres.length;
    private boolean visited[];

    @PostConstruct
    public void init() {
        for(Genre key : genres)
            mp.put(key, new ArrayList<>());
        mp.get(Genre.MYSTERY).addAll(Arrays.stream(new Genre[]{Genre.PSYCHOLOGICAL, Genre.MURDER, Genre.THRILL, Genre.FANTASY, Genre.ADVENTURE}).toList());
        mp.get(Genre.PSYCHOLOGICAL).addAll(Arrays.stream(new Genre[]{Genre.MYSTERY, Genre.MURDER, Genre.THRILL}).toList());
        mp.get(Genre.MURDER).addAll(Arrays.stream(new Genre[]{Genre.GORR}).toList());
        mp.get(Genre.GORR).addAll(Arrays.stream(new Genre[]{Genre.MURDER}).toList());
        mp.get(Genre.THRILL).addAll(Arrays.stream(new Genre[]{Genre.MYSTERY, Genre.PSYCHOLOGICAL, Genre.ACTION, Genre.SCHOOL}).toList());
        mp.get(Genre.ACTION).addAll(Arrays.stream(new Genre[]{Genre.FANTASY, Genre.ADVENTURE, Genre.THRILL}).toList());
        mp.get(Genre.FANTASY).addAll(Arrays.stream(new Genre[]{Genre.ACTION, Genre.ISEKAI, Genre.DRAMA, Genre.MYSTERY, Genre.KIDS}).toList());
        mp.get(Genre.KIDS).addAll(Arrays.stream(new Genre[]{Genre.FANTASY, Genre.SCHOOL, Genre.ISEKAI, Genre.SOFT, Genre.ADVENTURE}).toList());
        mp.get(Genre.ADVENTURE).addAll(Arrays.stream(new Genre[]{Genre.KIDS, Genre.LOVE, Genre.SCHOOL, Genre.MYSTERY}).toList());
        mp.get(Genre.DRAMA).addAll(Arrays.stream(new Genre[]{Genre.FANTASY, Genre.LOVE}).toList());
        mp.get(Genre.LOVE).addAll(Arrays.stream(new Genre[]{Genre.ADVENTURE, Genre.DRAMA, Genre.SOFT}).toList());
        mp.get(Genre.SCHOOL).addAll(Arrays.stream(new Genre[]{Genre.KIDS, Genre.ADVENTURE, Genre.THRILL}).toList());
        mp.get(Genre.ISEKAI).addAll(Arrays.stream(new Genre[]{Genre.KIDS, Genre.FANTASY}).toList());
        mp.get(Genre.SOFT).addAll(Arrays.stream(new Genre[]{Genre.LOVE, Genre.KIDS}).toList());
        indexMap.put(Genre.ACTION, 0);
        indexMap.put(Genre.ADVENTURE, 1);
        indexMap.put(Genre.DRAMA, 2);
        indexMap.put(Genre.FANTASY, 3);
        indexMap.put(Genre.GORR, 4);
        indexMap.put(Genre.ISEKAI, 5);
        indexMap.put(Genre.KIDS, 6);
        indexMap.put(Genre.LOVE, 7);
        indexMap.put(Genre.MURDER, 8);
        indexMap.put(Genre.MYSTERY, 9);
        indexMap.put(Genre.PSYCHOLOGICAL, 10);
        indexMap.put(Genre.SCHOOL, 11);
        indexMap.put(Genre.SOFT, 12);
        indexMap.put(Genre.THRILL, 13);
    }

    public Set<Genre> nearGenres(Genre source, int depth) {
        visited = new boolean[TOTAL];
        Queue<Genre> q = new ArrayDeque<>();
        q.add(source);
        final Set<Genre> genre = new HashSet<>();
        while(!q.isEmpty()) {
            // fixed: move depth-- below, else it may return empty set
            if(depth < 0)
                return genre;
            depth--;
            int sz = q.size();
            for(int i = 0; i < sz; i++) {
                Genre node = q.poll();
                if(node != source)
                    genre.add(node);
                visited[indexMap.get(node)] = true;
                for(Genre nextNode : mp.get(node))
                    if(!visited[indexMap.get(nextNode)])
                        q.add(nextNode);
            }
        }
        return genre;
    }

    public Set<Genre> allGenres() {return Arrays.stream(genres).collect(Collectors.toSet());}

    public Set<Genre> neighbors(Genre genre) {return mp.get(genre).stream().collect(Collectors.toSet());}

    public int total() {return TOTAL;}

    public Genre getFromIndex(int index) {return genres[index];}
}
