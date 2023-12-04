/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.dataadapter;

import java.math.BigDecimal;

import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.BaseDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.CurrencyDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.NumeroAccertamentoDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.NumeroProvvedimentoDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.PianoDeiContiDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.PlainNumberDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.SimpleDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.TypedDataInfo;

/**
 * Column Adapter per l'Accertamento
 * 
 *
 */
public class AccertamentoDataAdapter extends EntitaConsultabileDataAdapter {

	/**
	 * Costruttore vuoto di default
	 */
	public AccertamentoDataAdapter() {
		super(
			asList(
					new NumeroAccertamentoDataInfo("Accertamento", "right", "accertamento_desc", "accertamento_anno","accertamento_numero" ),
					new BaseDataInfo("Soggetto", "{0}-{1}","soggetto_code", "soggetto_desc"),
					new SimpleDataInfo("Stato", "accertamento_stato_desc"),
					new BaseDataInfo("Capitolo", "{0}/{1}/{2}", "capitolo_anno","capitolo_numero", "capitolo_articolo"),
					//SIAC-8188 "attoamm_oggetto", "attoal_causale"
					new NumeroProvvedimentoDataInfo("N. Provv.", "top", "attoamm_anno","attoamm_numero", "attoamm_tipo_code", "attoamm_tipo_desc" , "attoamm_sac_code", "attoamm_sac_desc", "attoamm_stato_desc", "attoamm_oggetto", "attoal_causale"),
					new PianoDeiContiDataInfo("Piano dei Conti", "left", "pdc_code", "pdc_desc"),
					new CurrencyDataInfo("Importo", "importo", true)

					),
			asList(
					//SIAC-3760
					new PlainNumberDataInfo("Accertamento", "accertamento_numero"),
					new PlainNumberDataInfo("Anno","accertamento_anno"),
					new SimpleDataInfo("Descrizione accertamento", "accertamento_desc").forceNoEscape(),
					new BaseDataInfo("Soggetto", "{0}-{1}","soggetto_code", "soggetto_desc").forceNoEscape(),
					new SimpleDataInfo("Stato", "accertamento_stato_desc").forceNoEscape(),
					//SIAC-8351
					new BaseDataInfo("Struttura Competente" , "{0} - {1}", "acc_sac_code", "acc_sac_desc").forceNoEscape(),
					
					//CAPITOLO
					new PlainNumberDataInfo("N.Capitolo", "capitolo_numero"),
					new PlainNumberDataInfo("N.Articolo", "capitolo_articolo"),
					new PlainNumberDataInfo("Anno capitolo", "capitolo_anno"),
					//SIAC-8351
					new BaseDataInfo("Struttura Amm. capitolo" , "{0} - {1}", "cap_sac_code", "cap_sac_desc").forceNoEscape(),

					//PROVVEDIMENYO
					new SimpleDataInfo("Anno provv.", "attoamm_anno"),
					new PlainNumberDataInfo("Numero provv.", "attoamm_numero"),
					new BaseDataInfo("Tipo provv." , "{0} - {1}", "attoamm_tipo_code", "attoamm_tipo_desc").forceNoEscape(),
					new SimpleDataInfo("Descrizione provvedimento","attoamm_oggetto").forceNoEscape(),
					new BaseDataInfo("Sruttura Provvedimento" , "{0} - {1}", "attoamm_sac_code", "attoamm_sac_desc").forceNoEscape(),
					new SimpleDataInfo("Stato provv.", "attoamm_stato_desc").forceNoEscape(),
					
					new BaseDataInfo("Piano dei Conti" , "{0} - {1}", "pdc_code", "pdc_desc").forceNoEscape(),
					//new BaseDataInfo("Soggetto", "{0}-{1}", "soggetto_code", "soggetto_desc").forceNoEscape(),
					new TypedDataInfo<BigDecimal>("Importo", "importo")

					),
			asList(
					new BaseDataInfo("testo", "Accertamento {0,number,#}", "accertamento_numero")
					)
);
	}
	  
	
}

/*
 * 2.9 Dettaglio dei requisiti: menu 6 – Cruscotto Consultazioni: “Accertamenti”
(rif. CR 960)
Per quanto riguarda la consultazione degli “Accertamenti” presente sul Cruscotto Consultazioni, si richiede di effettuare con i seguenti interventi:
Integrazione scarico Excel
Si richiede di aggiungere le seguenti colonne:
Descrizione del provvedimento (per uniformità con gli impegni)
Soggetto (riportato in modo analogo a quanto già fatto per gli impegni). ??? c’è già il soggetto!!!
*/
