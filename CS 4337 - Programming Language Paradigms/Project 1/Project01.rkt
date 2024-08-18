#lang racket

(define (char-numeric? c) (and (char>=? c #\0) (char<=? c #\9)))

; Evaluates a single expression
(define (expression-evaluation exp history)
  (cond [(char-numeric? (car exp)) 
         (let* [(value (string->number (list->string (list (car exp)))))
                (remaining (cdr exp))]
           (list value remaining))]

        [(char=? (car exp) #\+) 
         (let* [(left (expression-evaluation (cdr exp) history))
                (right (expression-evaluation (cadr left) history))]
           (list (+ (car left) (car right)) (cadr right)))]

        [(char=? (car exp) #\*) 
         (let* [(left (expression-evaluation (cdr exp) history))
                (right (expression-evaluation (cadr left) history))]
           (list (* (car left) (car right)) (cadr right)))]

        [(char=? (car exp) #\/) 
         (let* [(left (expression-evaluation (cdr exp) history))
                (right (expression-evaluation (cadr left) history))]
           (if (= (car right) 0)
               (list "Invalid Expression" (cadr right))
               (list (/ (car left) (car right)) (cadr right))))]

        [(char=? (car exp) #\-)
         (let [(result (expression-evaluation (cdr exp) history))]
           (list (- (car result)) (cadr result)))]

        [(char=? (car exp) #\$)
         (let* [(id (string->number (list->string (list (cadr exp)))))
                (value (list-ref history (- id 1)))]
           (list value (cddr exp)))]
   
        [else (error "Invalid Expression")]))

; Main Cal loop
(define (cal history count)
  (display "Enter Expression: ")
  (let* [(input (read-line))
         (expr (string->list input))]
    (if (string=? input "quit")
        (displayln "Program Exit")
        (let* [(result (expression-evaluation expr history))]
          (if (null? (cadr result))
              (let [(value (car result))]
                (display count) (display ": ") (displayln value)
                (cal (cons value history) (+ count 1)))
              (begin
                (displayln "Invalid Expression")
                (cal history count)))))))

; Start the program
(cal '() 1)