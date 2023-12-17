package com.example.account.service;

import com.example.account.dto.UseBalance;
import com.example.account.exception.AccountException;
import com.example.account.type.ErrorCode;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class LockAopAspectTest {

    @Mock
    private LockService lockService;

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

    @InjectMocks
    private LockAopAspect lockAopAspect;

    @Test
    void lockAndUnlock() throws Throwable {
        //given
        ArgumentCaptor<String> lockArgumentCaptor =
                ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> unLockArgumentCaptor =
                ArgumentCaptor.forClass(String.class);
        UseBalance.Request request =
                new UseBalance.Request(123L, "1234",1000L);


        //when
        lockAopAspect.aroundMethod(proceedingJoinPoint, request);


        //then
        Mockito.verify(lockService, Mockito.times(1))
                .lock(lockArgumentCaptor.capture());
        Mockito.verify(lockService, Mockito.times(1))
                .unLock(unLockArgumentCaptor.capture());
        assertEquals("1234",lockArgumentCaptor.getValue());
        assertEquals("1234",unLockArgumentCaptor.getValue());
    }

    @Test
    void lockAndUnlock_evenIfThrow() throws Throwable {
        //given
        ArgumentCaptor<String> lockArgumentCaptor =
                ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> unLockArgumentCaptor =
                ArgumentCaptor.forClass(String.class);
        UseBalance.Request request =
                new UseBalance.Request(123L, "54321",1000L);
        BDDMockito.given(proceedingJoinPoint.proceed())
                .willThrow(new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));
        //when
        assertThrows( AccountException.class,() ->
                lockAopAspect.aroundMethod(proceedingJoinPoint, request));
        //then
        Mockito.verify(lockService, Mockito.times(1))
                .lock(lockArgumentCaptor.capture());
        Mockito.verify(lockService, Mockito.times(1))
                .unLock(unLockArgumentCaptor.capture());
        assertEquals("54321",lockArgumentCaptor.getValue());
        assertEquals("54321",unLockArgumentCaptor.getValue());
    }
}