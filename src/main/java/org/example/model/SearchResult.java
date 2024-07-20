package org.example.model;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
@Getter
public class SearchResult {

    private List<String> result;

    private long took;

    private long hits;

    private boolean successful;

}
