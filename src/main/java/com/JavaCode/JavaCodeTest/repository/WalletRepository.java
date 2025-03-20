package com.JavaCode.JavaCodeTest.repository;

import com.JavaCode.JavaCodeTest.exceptions.NoSuchWalletException;
import com.JavaCode.JavaCodeTest.exceptions.NotEnoughBalanceException;
import com.JavaCode.JavaCodeTest.model.Wallet;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface WalletRepository extends CrudRepository<Wallet, UUID> {

    @Query("select w.balance from Wallet w where w.walletId = :walletId")
    Optional<BigDecimal> getBalanceByWalletId(UUID walletId);

    @Query("update Wallet w set w.balance = :amount where w.walletId = :walletId")
    @Modifying
    void changeAmount(UUID walletId, BigDecimal amount);


    default BigDecimal getBalanceById(UUID walletId) {
        return getBalanceByWalletId(walletId)
                .orElseThrow(() -> new NoSuchWalletException("No wallet with this id: " + walletId));
    }

    default void changeBalance(UUID walletId, BigDecimal amount) {
        BigDecimal curBalance = getBalanceById(walletId);
        if (curBalance.add(amount).compareTo(BigDecimal.ZERO) < 0)
            throw new NotEnoughBalanceException("Not enough balance. Please, top it up");
        changeAmount(walletId, curBalance.add(amount));
    }
}

//    @PersistenceContext
//    private EntityManager em;
//
//
//    public BigDecimal findBalanceByWalletId(UUID walletId) {
//            List<Wallet> wallet = em.createQuery("select balance from Wallet w where w.walletId = :id", Wallet.class)
//                    .setParameter("id", walletId)
//                    .getResultList();
//
//            if (wallet.isEmpty())
//                throw new NoSuchWalletException("The wallet doesn't exist");
//
//            return wallet.get(0).getBalance();
//    }

//    public void changeAmount(UUID walletId, BigDecimal amount) {
//
//        List<Wallet> wallet =
//                em.createQuery("select w from Wallet w where walletId = :id", Wallet.class)
//                                .setParameter("id", walletId)
//                                        .getResultList();
//
//        if (wallet.isEmpty())
//            throw new NoSuchWalletException("The wallet doesn't exist");
//
//        BigDecimal curBalance = wallet.get(0).getBalance();
//        wallet.get(0).setBalance(curBalance.add(amount));
//
//        if (wallet.get(0).getBalance().compareTo(BigDecimal.ZERO) < 0)
//            throw new NotEnoughBalanceException("Not enough money");
//
//        em.persist(wallet);
//    }

