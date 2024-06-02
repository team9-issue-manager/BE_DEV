package team9.issue_manage_system;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import team9.issue_manage_system.dto.CommentCreateDto;
import team9.issue_manage_system.dto.CommentReturnDto;
import team9.issue_manage_system.entity.Account;
import team9.issue_manage_system.entity.Comment;
import team9.issue_manage_system.entity.Issue;
import team9.issue_manage_system.repository.AccountRepository;
import team9.issue_manage_system.repository.CommentRepository;
import team9.issue_manage_system.repository.IssueRepository;
import team9.issue_manage_system.service.CommentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommentServiceTests {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private IssueRepository issueRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private CommentService commentService;

    private Comment comment;
    private Issue issue;
    private Account account;
    private CommentCreateDto commentCreateDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        account = new Account();
        account.setId("test_account");

        issue = new Issue();
        issue.setIssueNum(1L);

        comment = new Comment();
        comment.setCommentNum(1L);
        comment.setAccount(account);
        comment.setIssue(issue);
        comment.setContent("Test Comment");

        commentCreateDto = new CommentCreateDto();
        commentCreateDto.setAccountId("test_account");
        commentCreateDto.setIssueNum(1L);
        commentCreateDto.setContent("Test Comment");
    }

    @Test
    void testGetCommentsByIssueId() {
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);

        when(commentRepository.findByIssue_IssueNum(1L)).thenReturn(comments);

        List<CommentReturnDto> result = commentService.getCommentsByIssueId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Comment", result.get(0).getContent());
        verify(commentRepository, times(1)).findByIssue_IssueNum(1L);
    }

    @Test
    void testGetCommentById() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        Optional<CommentReturnDto> result = commentService.getCommentById(1L, 1L);

        assertTrue(result.isPresent());
        assertEquals("Test Comment", result.get().getContent());
        verify(commentRepository, times(1)).findById(1L);
    }

    @Test
    void testUploadCommentSuccess() {
        when(issueRepository.findById(1L)).thenReturn(Optional.of(issue));
        when(accountRepository.findById("test_account")).thenReturn(Optional.of(account));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Optional<CommentReturnDto> result = commentService.uploadComment(commentCreateDto);

        assertTrue(result.isPresent());
        assertEquals("Test Comment", result.get().getContent());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void testUploadCommentFail() {
        when(issueRepository.findById(1L)).thenReturn(Optional.empty());
        when(accountRepository.findById("test_account")).thenReturn(Optional.of(account));

        Optional<CommentReturnDto> result = commentService.uploadComment(commentCreateDto);

        assertFalse(result.isPresent());
        verify(commentRepository, never()).save(any(Comment.class));
    }
}

