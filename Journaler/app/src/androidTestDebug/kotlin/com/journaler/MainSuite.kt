package com.journaler

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
        MainServiceTest::class,
        NoteTest::class
)
class MainSuite