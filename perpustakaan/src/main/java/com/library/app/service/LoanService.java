package com.library.app.service;

import com.library.app.dao.BookCopyDAO;
import com.library.app.dao.LoanDAO;
import com.library.app.model.BookCopy;
import com.library.app.model.Loan;
import com.library.app.model.Member;
import com.library.app.model.enums.CopyStatus;
import com.library.app.model.enums.LoanStatus;
import com.library.app.util.ValidationUtil;
import com.library.app.util.DateUtil;
import com.library.app.util.FineCalculator;

import java.time.LocalDate;
import java.util.List;

public class LoanService {
    private static final int MAX_ACTIVE_LOANS = 3;

    private final LoanDAO loanDAO = new LoanDAO();
    private final BookCopyDAO bookCopyDAO = new BookCopyDAO();
    private final MemberService memberService = new MemberService();

    public Loan borrowBook(String memberCode, String isbn) {
        ValidationUtil.requireNotBlank(isbn, "ISBN wajib diisi.");
        Member member = memberService.findByCode(memberCode);
        String sanitizedIsbn = isbn.trim();

        int activeLoans = loanDAO.countActiveLoansByMember(member.getId());
        if (!member.canBorrow(activeLoans, MAX_ACTIVE_LOANS)) {
            throw new IllegalArgumentException("Anggota sudah mencapai batas maksimum pinjaman aktif.");
        }

        int availableCopies = bookCopyDAO.countAvailableByIsbn(sanitizedIsbn);
        if (availableCopies <= 0) {
            throw new IllegalArgumentException("Buku tidak tersedia untuk dipinjam berdasarkan ISBN tersebut.");
        }

        BookCopy copy = bookCopyDAO.findFirstAvailableByIsbn(sanitizedIsbn)
                .orElseThrow(() -> new IllegalArgumentException("Buku tidak tersedia untuk dipinjam berdasarkan ISBN tersebut."));

        Loan loan = new Loan();
        loan.setMemberId(member.getId());
        loan.setCopyId(copy.getId());
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(DateUtil.calculateDueDate(LocalDate.now()));
        loanDAO.save(loan);

        copy.markBorrowed();
        bookCopyDAO.updateStatus(copy.getId(), copy.getStatus());
        return loan;
    }

    public Loan returnBook(String copyCode) {
        ValidationUtil.requireNotBlank(copyCode, "Kode eksemplar wajib diisi.");
        Loan loan = loanDAO.findActiveLoanByCopyCode(copyCode.trim())
                .orElseThrow(() -> new IllegalArgumentException("Transaksi pinjam aktif tidak ditemukan untuk kode eksemplar ini."));
        loan.setReturnDate(LocalDate.now());
        long daysLate = DateUtil.calculateDaysDifference(loan.getDueDate(), loan.getReturnDate());
        loan.setFineAmount(FineCalculator.calculateTotalFine(daysLate));
        loan.setStatus(LoanStatus.RETURNED);
        loanDAO.updateReturn(loan);
        bookCopyDAO.updateStatus(loan.getCopyId(), CopyStatus.AVAILABLE);
        return loan;
    }

    public List<Loan> getActiveLoans() {
        return loanDAO.findActiveLoans();
    }

    public List<Loan> getReturnedLoans() {
        return loanDAO.findReturnedLoans();
    }
}
