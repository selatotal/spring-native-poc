package br.com.selat.springnativepoc.controller;

import br.com.selat.springnativepoc.model.ErrorException;
import br.com.selat.springnativepoc.model.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculatorController {

    @GetMapping("/sum/{number1}/{number2}")
    private Result sum(@PathVariable("number1") Double number1,
                       @PathVariable("number2") Double number2){
        validateNumbers(number1, number2);
        return new Result(number1 + number2);
    }

    @GetMapping("/sub/{number1}/{number2}")
    private Result sub(@PathVariable("number1") Double number1,
                       @PathVariable("number2") Double number2){
        validateNumbers(number1, number2);
        return new Result(number1 - number2);
    }

    @GetMapping("/mul/{number1}/{number2}")
    private Result mul(@PathVariable("number1") Double number1,
                       @PathVariable("number2") Double number2){
        validateNumbers(number1, number2);
        return new Result(number1 * number2);
    }

    @GetMapping("/div/{number1}/{number2}")
    private Result div(@PathVariable("number1") Double number1,
                       @PathVariable("number2") Double number2){
        validateNumbers(number1, number2);
        if (number2.equals(0D)){
            throw new ErrorException("Divide by zero");
        }
        return new Result(number1 / number2);
    }

    private void validateNumbers(Double number1, Double number2){
        if (number1 == null){
            throw new ErrorException("Invalid number1");
        }
        if (number2 == null){
            throw new ErrorException("Invalid number2");
        }
    }

}
