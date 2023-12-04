/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacbilapp.frontend.ui.model.previmpacc;

import java.util.Set;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaStanziamentiCapitoloGestione;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.PrevisioneImpegnatoAccertato;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;

/**
 * The Class RicercaPrevisioneImpegnatoAccertatoModel.
 *
 * @author elisa
 * @version 1.0.0 15 ott 2021
 */
public abstract class ConsultaPrevisioneImpegnatoAccertatoModel extends GenericBilancioModel {

	/** per la serializzazione */
	private static final long serialVersionUID = 7870940541968266689L;

	private Integer uidCapitolo;
	private PrevisioneImpegnatoAccertato previsioneImpegnatoAccertato;
	private ImportiCapitolo importiCapitolo0;
	private ImportiCapitolo importiCapitolo1;
	private ImportiCapitolo importiCapitolo2;
	private Capitolo<?,?> capitolo;
	private ElementoPianoDeiConti elementoPianoDeiConti;
	private StrutturaAmministrativoContabile strutturaAmministrativoContabile;
	
	/** Costruttore vuoto di default */
	public ConsultaPrevisioneImpegnatoAccertatoModel() {
		super();
		setTitolo("Consulta previsione su capitolo");
	}
	
	
	public Integer getUidCapitolo() {
		return uidCapitolo;
	}


	public void setUidCapitolo(Integer uidCapitolo) {
		this.uidCapitolo = uidCapitolo;
	}


	public PrevisioneImpegnatoAccertato getPrevisioneImpegnatoAccertato() {
		return previsioneImpegnatoAccertato;
	}


	public void setPrevisioneImpegnatoAccertato(PrevisioneImpegnatoAccertato previsioneImpegnatoAccertato) {
		this.previsioneImpegnatoAccertato = previsioneImpegnatoAccertato;
	}

	public ImportiCapitolo getImportiCapitolo0() {
		return importiCapitolo0;
	}


	public void setImportiCapitolo0(ImportiCapitolo importiCapitolo0) {
		this.importiCapitolo0 = importiCapitolo0;
	}


	public ImportiCapitolo getImportiCapitolo1() {
		return importiCapitolo1;
	}


	public void setImportiCapitolo1(ImportiCapitolo importiCapitolo1) {
		this.importiCapitolo1 = importiCapitolo1;
	}


	public ImportiCapitolo getImportiCapitolo2() {
		return importiCapitolo2;
	}


	public void setImportiCapitolo2(ImportiCapitolo importiCapitolo2) {
		this.importiCapitolo2 = importiCapitolo2;
	}
	
	public Capitolo<?, ?> getCapitolo() {
		return capitolo;
	}


	public void setCapitolo(Capitolo<?, ?> capitolo) {
		this.capitolo = capitolo;
	}

	public ElementoPianoDeiConti getElementoPianoDeiConti() {
		return elementoPianoDeiConti;
	}


	public void setElementoPianoDeiConti(ElementoPianoDeiConti elementoPianoDeiConti) {
		this.elementoPianoDeiConti = elementoPianoDeiConti;
	}


	public StrutturaAmministrativoContabile getStrutturaAmministrativoContabile() {
		return strutturaAmministrativoContabile;
	}


	public void setStrutturaAmministrativoContabile(StrutturaAmministrativoContabile strutturaAmministrativoContabile) {
		this.strutturaAmministrativoContabile = strutturaAmministrativoContabile;
	}


	public boolean isPrevisioneEditabile() {
		return false;
	}
	
	public abstract String getDescrizioneImportoDerivato();
	public abstract String getImportoDerivatoAnno0();
	public abstract String getImportoDerivatoAnno1();
	public abstract String getImportoDerivatoAnno2();
	protected abstract  Set<ImportiCapitoloEnum> caricaImportiDerivatiRichiesti();
	public abstract String getIdentificativoPrevisione();
	
	public RicercaStanziamentiCapitoloGestione creaRequestRicercaStanziamentiCapitolo() {
		RicercaStanziamentiCapitoloGestione request = creaRequest(RicercaStanziamentiCapitoloGestione.class);
		Set<ImportiCapitoloEnum> importiDerivati = caricaImportiDerivatiRichiesti();
		request.setUidCapitolo(getUidCapitolo());
		request.setImportiDerivatiRichiesti(importiDerivati);
		return request;
	}
}
