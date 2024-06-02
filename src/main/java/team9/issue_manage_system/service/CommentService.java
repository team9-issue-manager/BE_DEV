package team9.issue_manage_system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team9.issue_manage_system.dto.CommentCreateDto;
import team9.issue_manage_system.dto.CommentReturnDto;
import team9.issue_manage_system.entity.Account;
import team9.issue_manage_system.entity.Comment;
import team9.issue_manage_system.entity.Issue;
import team9.issue_manage_system.repository.AccountRepository;
import team9.issue_manage_system.repository.CommentRepository;
import team9.issue_manage_system.repository.IssueRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final IssueRepository issueRepository;
    private final AccountRepository accountRepository;

    public List<CommentReturnDto> getCommentsByIssueId(Long issueNum) {
        List<Comment> comments = commentRepository.findByIssue_IssueNum(issueNum);
        List<CommentReturnDto> commentReturnDtos = new ArrayList<>();

        for (Comment comment : comments){
            CommentReturnDto commentReturnDto = makeCommentReturnDto(comment);
            commentReturnDtos.add(commentReturnDto);
        }
        return commentReturnDtos;
    }

    public Optional<CommentReturnDto> getCommentById(Long commentId) {
         Optional<Comment> commentOpt = commentRepository.findById(commentId);
         if (commentOpt.isPresent()) {
             Comment comment = commentOpt.get();
             CommentReturnDto commentReturnDto = makeCommentReturnDto(comment);
             return Optional.of(commentReturnDto);
         }
         else {
             return Optional.empty();
         }
    }

    public Optional<CommentReturnDto> uploadComment(CommentCreateDto commentCreateDto) {
        Optional<Issue> issueOpt = issueRepository.findById(commentCreateDto.getIssueNum());
        Optional<Account> accountOpt = accountRepository.findById(commentCreateDto.getAccountId());

        if (issueOpt.isPresent() && accountOpt.isPresent()) {
            Issue issue = issueOpt.get();
            Account account = accountOpt.get();
            Comment comment = new Comment();
            comment.setContent(commentCreateDto.getContent());
            comment.setAccount(account);
            comment.setIssue(issue);
            commentRepository.save(comment);

            CommentReturnDto commentReturnDto = makeCommentReturnDto(comment);
            return Optional.of(commentReturnDto);
        } else {
            return Optional.empty();
        }
    }

    public CommentReturnDto makeCommentReturnDto(Comment comment) {
        CommentReturnDto commentReturnDto = new CommentReturnDto();
        commentReturnDto.setCommentNum(comment.getCommentNum());
        commentReturnDto.setAccountId(comment.getAccount().getId());
        commentReturnDto.setDate(comment.getDate());
        commentReturnDto.setContent(comment.getContent());
        commentReturnDto.setIssueNum(comment.getIssue().getIssueNum());

        return commentReturnDto;
    }

}

