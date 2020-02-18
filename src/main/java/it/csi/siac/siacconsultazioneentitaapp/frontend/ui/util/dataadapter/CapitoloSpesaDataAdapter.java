/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.dataadapter;

import java.math.BigDecimal;

import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.BaseDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.ClassificazioneCapitoloSpesaDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.DenominazioneCapitoloDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.SimpleDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.TypedDataInfo;

/**
 * Column Adapter per il Capitolo di Spesa
 * 
 *
 */
public class CapitoloSpesaDataAdapter extends EntitaConsultabileDataAdapter {
	
	/**
	 * 
	 */
	public CapitoloSpesaDataAdapter() {
		super(asList(
						new DenominazioneCapitoloDataInfo("Capitolo", "left", 
									"capitolo_desc", 
									"articolo_desc",
									"elem_tipo_code", 
									"capitolo_anno",
									"capitolo_numero",
									"capitolo_articolo"),
						new ClassificazioneCapitoloSpesaDataInfo("Classificazione", "right", 
						 			"classif_missione_code", 
									"classif_missione_desc", 
									"classif_programma_code", 
									"classif_programma_desc", 
									"classif_titolo_code", 
									"classif_titolo_desc", 
									"classif_macr_code",
									"classif_macr_desc"),
						new BaseDataInfo("Strutt. Amm. Resp.","{0} - {1}", "classif_sac_code", "classif_sac_desc"),
						new SimpleDataInfo("P.d.c. finanziario", "classif_pdc_code")
						),
				asList(
						new SimpleDataInfo("Capitolo", "capitolo_numero"),
						new SimpleDataInfo("Anno", "capitolo_anno"),
						new SimpleDataInfo("Articolo", "capitolo_articolo"),
						new SimpleDataInfo("Tipo Capitolo", "elem_tipo_code"),
						new SimpleDataInfo("Descrizione Capitolo", "capitolo_desc").forceNoEscape(),
						new SimpleDataInfo("Descrizione Articolo", "articolo_desc").forceNoEscape(),
						new BaseDataInfo("Missione", "{0} - {1}", "classif_missione_code", "classif_missione_desc").forceNoEscape(),
						new BaseDataInfo("Programma", "{0} - {1}", "classif_programma_code", "classif_programma_desc").forceNoEscape(),
						new BaseDataInfo("Titolo", "{0} - {1}", "classif_titolo_code", "classif_titolo_desc").forceNoEscape(),
						new BaseDataInfo("Macroaggregato", "{0} - {1}", "classif_macr_code", "classif_macr_desc").forceNoEscape(),
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
						new BaseDataInfo("testo", "Capitolo Spesa {0}/{1}/{2}", 
								"capitolo_anno",
								"capitolo_numero", 
								"capitolo_articolo")
						
						)
			);
	}
	 
	
}
