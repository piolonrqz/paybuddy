package com.splitwise.Controller;

import com.splitwise.DTO.BalanceDTO;
import com.splitwise.DTO.SimplifiedDebtDTO;
import com.splitwise.Service.BalanceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/balance")
public class BalanceController {

    private final BalanceService balanceService;

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    // ✅ Get all balances
    @GetMapping("/all")
    public List<BalanceDTO> getAllBalances() {
        return balanceService.calculateBalances();
    }

    // ✅ Get single user balance
    @GetMapping("/{userId}")
    public BalanceDTO getUserBalance(@PathVariable Long userId) {
        return balanceService.getUserBalance(userId);
    }

    // ✅ Get simplified debts
    @GetMapping("/simplify")
    public List<SimplifiedDebtDTO> simplifyDebts() {
        return balanceService.simplifyDebts();
    }
}
