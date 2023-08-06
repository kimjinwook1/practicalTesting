package sample.cafekiosk.spring.api.service.mail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sample.cafekiosk.spring.client.mail.MailSendClient;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    @Mock
    MailSendClient mailSendClient;
    @Mock
    MailSendHistoryRepository mailSendHistoryRepository;
    @InjectMocks
    MailService mailService;

    @Test
    @DisplayName("메일 전송 테스트")
    void sendMail() {
        //given
        // 스프링을 사용하지 않고 순수한 Mock 객체 만드는 방식
//        final MailSendClient mailSendClient = mock(MailSendClient.class);
//        final MailSendHistoryRepository mailSendHistoryRepository = mock(MailSendHistoryRepository.class);

//        final MailService mailService = new MailService(mailSendClient, mailSendHistoryRepository);

        // @Mock 어노테이션을 사용했을 때 문법
//        when(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
//                .thenReturn(true);

        // !!추천 -> Bdd Mockito 사용했을 때 문법 (@Mock 어노테이션 사용해야함)
        given(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
                .willReturn(true);

        // @Spy 어노테이션을 사용했을 때 문법
//        doReturn(true)
//                .when(mailSendClient)
//                .sendEmail(anyString(), anyString(), anyString(), anyString());

        //when
        final boolean result = mailService.sendMail("", "", "", "");

        //then
        assertThat(result).isTrue();
        verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));

    }

}
