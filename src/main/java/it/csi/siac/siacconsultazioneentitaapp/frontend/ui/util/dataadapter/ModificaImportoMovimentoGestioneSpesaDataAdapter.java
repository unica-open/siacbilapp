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
 * Column Adapter per la modifica dell'importo per il movimento di gestione
 * 
 *
 */
public class ModificaImportoMovimentoGestioneSpesaDataAdapter extends EntitaConsultabileDataAdapter {

	/**
	 * Costruttore vuoto di default
	 */
	public ModificaImportoMovimentoGestioneSpesaDataAdapter() {
		super(
			asList(
				new NumeroMovimentoGestioneTestataSubDataInfo("Impegno", "left", "impegno_desc", "impegno_anno", "impegno_numero", "subimpegno_numero", "impegno_subimpegno"),
				new BaseDataInfo("Numero", "<span class=\"pull-right\">{0}</span>", "modifica_numero"),
				new SimpleDataInfo("Descrizione", "modifica_desc"),
				new BaseDataInfo("Motivo", "{0} - {1}","modifica_tipo_code", "modifica_tipo_desc"),
				new PopoverDataInfo("Stato", "{0}", "left", "Descrizione", "{1}", "modifica_stato_desc", "modifica_stato_code"),
				new CurrencyDataInfo("Importo", "importo")
			),
			asList(
				new PlainNumberDataInfo("Impegno", "impegno_numero"),
				new NumeroSubMovimentoGestioneDataInfo("Subimpegno", "subimpegno_numero", "impegno_subimpegno"),
				//new PlainNumberDataInfo("Anno","impegno_anno"),
				new SimpleDataInfo("Anno","impegno_anno"),
				new SimpleDataInfo("Descrizione impegno", "impegno_desc").forceNoEscape(),
				new SimpleDataInfo("Numero", "modifica_numero"),
				new SimpleDataInfo("Descrizione Modifica", "modifica_desc").forceNoEscape(),
				new BaseDataInfo("Motivo", "{0} - {1}", "modifica_tipo_code", "modifica_tipo_desc").forceNoEscape(),
				new BaseDataInfo("Stato", "{0} - {1}", "modifica_stato_code", "modifica_stato_desc").forceNoEscape(),
				new TypedDataInfo<BigDecimal>("Importo", "importo")
			),
			asList(
				new BaseDataInfo("testo", "Modifica spesa {0,number,#}", "modifica_numero")
			));
		}
	
}
