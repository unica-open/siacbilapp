/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacbilapp.frontend.ui.model.previmpacc;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioModulareCapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siacfin2ser.model.CapitoloUscitaGestioneModelDetail;

/**
 * The Class RicercaPrevisioneImpegnatoAccertatoModel.
 *
 * @author elisa
 * @version 1.0.0 15 ott 2021
 */
public class ConsultaPrevisioneImpegnatoCapUGModel extends ConsultaPrevisioneImpegnatoAccertatoModel {
	
	/** per la serializzazione */
	private static final long serialVersionUID = -8384743304142058860L;
	
	public ConsultaPrevisioneImpegnatoCapUGModel() {
		super();
		setTitolo("Consulta previsione impegnato");
	}
	
	@Override
	public String getDescrizioneImportoDerivato() {
		return "Impegnato";
	}

	@Override
	public String getImportoDerivatoAnno0() {
		ImportiCapitolo importiCapitolo0 = getImportiCapitolo0();
		if(importiCapitolo0 instanceof ImportiCapitoloUG) {
			BigDecimal derivatoAnno0 = ((ImportiCapitoloUG) importiCapitolo0).getImpegnatoEffettivoUGAnno1();
			return FormatUtils.formatCurrency(derivatoAnno0);
		}
		return "N.D.";
	}

	@Override
	public String getImportoDerivatoAnno1() {
		ImportiCapitolo importiCapitolo0 = getImportiCapitolo0();
		if(importiCapitolo0 instanceof ImportiCapitoloUG) {
			BigDecimal derivatoAnno1 = ((ImportiCapitoloUG) importiCapitolo0).getImpegnatoEffettivoUGAnno2();
			return FormatUtils.formatCurrency(derivatoAnno1);
		}
		return "N.D.";
	}

	@Override
	public String getImportoDerivatoAnno2() {
		ImportiCapitolo importiCapitolo0 = getImportiCapitolo0();
		if(importiCapitolo0 instanceof ImportiCapitoloUG) {
			BigDecimal derivatoAnno0 = ((ImportiCapitoloUG) importiCapitolo0).getImpegnatoEffettivoUGAnno3();
			return FormatUtils.formatCurrency(derivatoAnno0);
		}
		return "N.D.";
	}
	
	@Override
	protected Set<ImportiCapitoloEnum> caricaImportiDerivatiRichiesti() {
		Set<ImportiCapitoloEnum> importiDerivati = new HashSet<ImportiCapitoloEnum>();
		importiDerivati.add(ImportiCapitoloEnum.impegnatoEffettivoUGAnno1);
		importiDerivati.add(ImportiCapitoloEnum.impegnatoEffettivoUGAnno2);
		importiDerivati.add(ImportiCapitoloEnum.impegnatoEffettivoUGAnno3);
		return importiDerivati;
	}

	
	public RicercaDettaglioModulareCapitoloUscitaGestione creaRequestRicercaDettaglioModulareCapitoloUscitaGestione() {
		RicercaDettaglioModulareCapitoloUscitaGestione req = creaRequest(RicercaDettaglioModulareCapitoloUscitaGestione.class);
		req.setCapitoloUscitaGestione(new CapitoloUscitaGestione()); 
		req.getCapitoloUscitaGestione().setUid(getUidCapitolo());
		req.setModelDetails(CapitoloUscitaGestioneModelDetail.Stato, CapitoloUscitaGestioneModelDetail.Classificatori , CapitoloUscitaGestioneModelDetail.Categoria, CapitoloUscitaGestioneModelDetail.AnnoCreazione);
		req.setTipologieClassificatoriRichiesti(TipologiaClassificatore.CDC, TipologiaClassificatore.CDC, TipologiaClassificatore.PDC_IV, TipologiaClassificatore.PDC_V, TipologiaClassificatore.MISSIONE);
		return req;
	}
	
	@Override
	public String getIdentificativoPrevisione() {
		return "PrevisioneImpegnatoCapUG";
	}

}
