     D  number1        S              7  0 inz(*Zeros)
     D  number2        S              7  0 inz(*Zeros)
     D  msg            S             50
      *
      /FREE
        msg = 'Hello world';
        dsply msg;
        msg = '你好世界';
        dsply 'Hello world in Chinese: ' + msg;
        number1 = 5;
        number2 = 3;
        dsply 'number1 * number2 = ' + %char(number1 * number2);
      /END-FREE