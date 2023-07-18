package ru.practicum.event.web.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.event.db.model.Comment;
import ru.practicum.event.db.model.Event;
import ru.practicum.event.web.dto.CommentDto;
import ru.practicum.user.db.model.User;

@UtilityClass
public class CommentMapper {


    public static Comment toComment(CommentDto commentDto, User user, Event event) {
        return new Comment(commentDto.getId(), commentDto.getText(), event, user, commentDto.getCreated());
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(), comment.getAuthor().getName(),
                comment.getEvent().getId(), comment.getCreated());
    }

}