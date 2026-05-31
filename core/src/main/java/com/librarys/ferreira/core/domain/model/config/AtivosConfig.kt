package com.librarys.ferreira.core.domain.model.config

import com.librarys.ferreira.core.domain.model.enums.PropFirm
import com.librarys.ferreira.core.domain.model.model.Ativos
import com.librarys.ferreira.core.domain.model.model.SymbolAtivo

object AtivosConfig {

    internal val listAtivos = listOf(
        Ativos(
            symbol = SymbolAtivo.MNQ,
            name = "Micro Nasdaq"
        ),
        Ativos(
            symbol = SymbolAtivo.NQ,
            name = "Nasdaq"
        ),
        Ativos(
            symbol = SymbolAtivo.ES,
            name = "S&P 500"
        )
    )

    internal val costsAtivosMap = mapOf(
        (PropFirm.YLOS_TRADING to SymbolAtivo.MNQ) to 1.3,
        (PropFirm.YLOS_TRADING to SymbolAtivo.NQ) to 4.36,
        (PropFirm.YLOS_TRADING to SymbolAtivo.ES) to 4.36,
        (PropFirm.TRADEIFY to SymbolAtivo.MNQ) to 1.82,
        (PropFirm.TRADEIFY to SymbolAtivo.NQ) to 5.76,
        (PropFirm.TRADEIFY to SymbolAtivo.ES) to 5.76,
        (PropFirm.LUCID_TRADING to SymbolAtivo.MNQ) to 1.0,
        (PropFirm.LUCID_TRADING to SymbolAtivo.NQ) to 3.50,
        (PropFirm.LUCID_TRADING to SymbolAtivo.ES) to 3.50,
    )

}