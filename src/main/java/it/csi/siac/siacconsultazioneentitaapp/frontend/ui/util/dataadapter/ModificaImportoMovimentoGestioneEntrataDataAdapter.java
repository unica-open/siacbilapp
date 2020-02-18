/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.dataadapter;

import java.math.BigDecimal;

import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.BaseDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.CurrencyDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.NumeroMovimentoGestioneTestataSubDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.NumeroSubMovimentoGestioneDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.PlainNumberDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.PopoverDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.SimpleDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.TypedDataInfo;

/**
 * Column Adapter per la Modifica di importo del movimento di gestione di spesa
 * 
 *
 */
public class ModificaImportoMovimentoGestioneEntrataDataAdapter extends EntitaConsultabileDataAdapter {

	/**
	 * Costruttore vuoto di default
	 */
	public ModificaImportoMovimentoGestioneEntrataDataAdapter() {
		super(
			asList(
				new NumeroMovimentoGestioneTestataSubDataInfo("Accertamento", "left", "accertamento_desc", "accertamento_anno", "accertamento_numero", "subaccertamento_numero", "accertamento_subaccertamento"),
				new BaseDataInfo("Numero", "<span class=\"pull-right\">{0}</span>", "modifica_numero"),
				new SimpleDataInfo("Descrizione", "modifica_desc"),
				new BaseDataInfo("Motivo", "{0} - {1}","modifica_tipo_code", "modifica_tipo_desc"),
				new PopoverDataInfo("Stato", "{0}", "left", "Descrizione", "{1}", "modifica_stato_desc", "modifica_stato_code"),
				new CurrencyDataInfo("Importo", "importo")
			),
			asList(
				new PlainNumberDataInfo("Accertamento", "accertamento_numero"),
				new NumeroSubMovimentoGestioneDataInfo("Subaccertamento", "subaccertamento_numero", "accertamento_subaccertamento"),
				new PlainNumberDataInfo("Anno","accertamento_anno"),
				new SimpleDataInfo("Descrizione accertamento", "accertamento_desc").forceNoEscape(),
				new SimpleDataInfo("Numero", "modifica_numero"),
				new SimpleDataInfo("Descrizione Modifica", "modifica_desc").forceNoEscape(),
				new BaseDataInfo("Motivo", "{0} - {1}", "modifica_tipo_code", "modifica_tipo_desc").forceNoEscape(),
				new BaseDataInfo("Stato", "{0} - {1}", "modifica_stato_code", "modifica_stato_desc").forceNoEscape(),
				new TypedDataInfo<BigDecimal>("Importo", "importo")
			),
			asList(
				new BaseDataInfo("testo", "Modifica entrata {0,number,#}", "modifica_numero")
			));
	}
	
}
