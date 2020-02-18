/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.dataadapter;

import java.math.BigDecimal;

import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.BaseDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.ClassificazioneCapitoloEntrataDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.DenominazioneCapitoloDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.SimpleDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.TypedDataInfo;

/**
 * Column Adapter per il Capitolo di Entrata
 * 
 *
 */
public class CapitoloEntrataDataAdapter extends EntitaConsultabileDataAdapter {

	/**
	 * Costruttore vuoto di default.
	 */
	public CapitoloEntrataDataAdapter() {
		super(
				asList(
					new DenominazioneCapitoloDataInfo("Capitolo", "left", 
						"capitolo_desc", 
						"articolo_desc",
						"elem_tipo_code", 
						"capitolo_anno",
						"capitolo_numero",
						"capitolo_articolo"),
					new ClassificazioneCapitoloEntrataDataInfo("Classificazione", "right", 
						"classif_titolo_code", 
						"classif_titolo_desc", 
						"classif_tipologia_code", 
						"classif_tipologia_desc",  
						"classif_categoria_code",
						"classif_categoria_desc"),
					new BaseDataInfo("Strutt. Amm. Resp.","{0} - {1}", "classif_sac_code", "classif_sac_desc"),
					new SimpleDataInfo("P.d.c. finanziario", "classif_pdc_code")
					),
				asList(
					new SimpleDataInfo("N.Capitolo", "capitolo_numero"),
					new SimpleDataInfo("N.Articolo", "capitolo_articolo"),
					new SimpleDataInfo("Anno capitolo", "capitolo_anno"),
					new SimpleDataInfo("Tipo Capitolo", "elem_tipo_code").forceNoEscape(),
					new SimpleDataInfo("Descrizione Capitolo", "capitolo_desc").forceNoEscape(),
					new SimpleDataInfo("Descrizione Articolo", "articolo_desc").forceNoEscape(),
					new BaseDataInfo("Categoria", "{0} - {1}", "classif_categoria_code", "classif_categoria_desc").forceNoEscape(),
					new BaseDataInfo("Tipologia", "{0} - {1}", "classif_tipologia_code", "classif_tipologia_desc").forceNoEscape(),
					new BaseDataInfo("Titolo", "{0} - {1}", "classif_titolo_code", "classif_titolo_desc").forceNoEscape(),
					new BaseDataInfo("Strutt. Amm. Resp.","{0} - {1}", "classif_sac_code", "classif_sac_desc").forceNoEscape(),
					new SimpleDataInfo("P.d.c. finanziario", "classif_pdc_code").forceNoEscape(),
					// SIAC-4669
					new TypedDataInfo<BigDecimal>("Stanziamento", "stanziamento"),
					new TypedDataInfo<BigDecimal>("Residuo", "stanziamento_residuo"),
					new TypedDataInfo<BigDecimal>("Cassa", "stanziamento_cassa"),
					new TypedDataInfo<BigDecimal>("Variazione stanziamento (bozza)", "stanziamento_var"),
					new TypedDataInfo<BigDecimal>("Variazione residuo (bozza)", "stanziamento_res_var"),
					new TypedDataInfo<BigDecimal>("Variazione cassa (bozza)", "stanziamento_cassa_var")
					),
				asList(
					new BaseDataInfo("testo", "Capitolo Entrata {0}/{1}/{2}", "capitolo_anno", "capitolo_numero", "capitolo_articolo")
					)
			);
	}
	  
	
}
