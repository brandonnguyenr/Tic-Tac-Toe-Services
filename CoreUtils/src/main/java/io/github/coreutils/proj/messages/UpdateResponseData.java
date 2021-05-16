package io.github.coreutils.proj.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateResponseData {
    private final UpdateData data;
    private final boolean updateSuccess;
    private final String info;
}
